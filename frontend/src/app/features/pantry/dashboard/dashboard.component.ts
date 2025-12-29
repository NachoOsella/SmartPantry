import { Component, OnInit, signal, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { trigger, transition, style, animate, query, stagger } from '@angular/animations';
import { AuthService } from '../../../core/services/auth.service';
import { ProductService } from '../../../core/services/product.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Product, Category } from '../../../core/models/pantry.model';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { CardComponent } from '../../../shared/components/card/card.component';
import { InputComponent } from '../../../shared/components/input/input.component';
import { SidePanelComponent } from '../../../shared/components/side-panel/side-panel.component';
import { ConfirmModalComponent } from '../../../shared/components/confirm-modal/confirm-modal.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    ButtonComponent, 
    CardComponent, 
    InputComponent, 
    SidePanelComponent,
    ConfirmModalComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  animations: [
    trigger('listAnimation', [
      transition('* <=> *', [
        query(':enter', [
          style({ opacity: 0, transform: 'translateY(10px)' }),
          stagger('50ms', [
            animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
          ])
        ], { optional: true })
      ])
    ])
  ]
})
export class DashboardComponent implements OnInit {
  authService = inject(AuthService);
  private productService = inject(ProductService);
  private notificationService = inject(NotificationService);
  private fb = inject(FormBuilder);

  products = signal<Product[]>([]);
  categories = signal<Category[]>([]);
  isLoading = signal(true);
  searchTerm = signal('');
  
  filteredProducts = computed(() => {
    const term = this.searchTerm().toLowerCase();
    return this.products().filter(p => 
      p.name.toLowerCase().includes(term) || 
      p.categoryName.toLowerCase().includes(term)
    );
  });

  expiringSoonCount = computed(() => 
    this.products().filter(p => p.expiryStatus !== 'GREEN').length
  );
  
  isPanelOpen = signal(false);
  isSaving = false;
  editingProduct = signal<Product | null>(null);

  isDeleteModalOpen = signal(false);
  productToDeleteId = signal<number | null>(null);

  isAddingCategory = signal(false);
  isSavingCategory = false;
  
  statusMap = {
    'GREEN': { label: 'Fresh', class: 'green' },
    'YELLOW': { label: 'Expiring Soon', class: 'yellow' },
    'RED': { label: 'Expired', class: 'red' }
  };

  productForm: FormGroup = this.fb.group({
    name: ['', Validators.required],
    quantity: [1, [Validators.required, Validators.min(1)]],
    expirationDate: ['', Validators.required],
    categoryId: ['', Validators.required]
  });

  newCategoryControl = new FormControl('', [Validators.required]);

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.isLoading.set(true);
    this.productService.getProducts().subscribe({
      next: (products) => {
        this.products.set(products);
        this.isLoading.set(false);
      },
      error: () => {
        this.notificationService.error('Failed to load inventory.');
        this.isLoading.set(false);
      }
    });

    this.productService.getCategories().subscribe({
      next: (categories) => {
        this.categories.set(categories);
        if (categories.length > 0 && !this.productForm.get('categoryId')?.value) {
          this.productForm.patchValue({ categoryId: categories[0].id });
        }
      }
    });
  }

  updateSearch(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.searchTerm.set(value);
  }

  openCreatePanel() {
    this.editingProduct.set(null);
    this.productForm.reset({
      quantity: 1,
      categoryId: this.categories()[0]?.id || ''
    });
    this.openModal();
  }

  openEditPanel(product: Product) {
    this.editingProduct.set(product);
    this.productForm.patchValue({
      name: product.name,
      quantity: product.quantity,
      expirationDate: product.expirationDate.split('T')[0],
      categoryId: product.categoryId
    });
    this.openModal();
  }

  private openModal() {
    this.isPanelOpen.set(true);
    document.body.classList.add('scroll-lock');
  }

  closePanel() {
    this.isPanelOpen.set(false);
    this.isAddingCategory.set(false);
    this.newCategoryControl.reset();
    document.body.classList.remove('scroll-lock');
  }

  toggleNewCategory() {
    this.isAddingCategory.set(!this.isAddingCategory());
    this.newCategoryControl.reset();
  }

  saveCategory() {
    const name = this.newCategoryControl.value?.trim();
    if (name && this.newCategoryControl.valid) {
      this.isSavingCategory = true;
      this.productService.createCategory({ name }).subscribe({
        next: (newCat) => {
          this.isSavingCategory = false;
          this.isAddingCategory.set(false);
          this.newCategoryControl.reset();
          this.notificationService.success(`Category "${name}" created.`);
          
          this.productService.getCategories().subscribe(categories => {
            this.categories.set(categories);
            this.productForm.patchValue({ categoryId: newCat.id });
          });
        },
        error: (err) => {
          this.isSavingCategory = false;
          this.notificationService.error(err.error?.message || 'Error creating category.');
        }
      });
    }
  }

  saveProduct() {
    if (this.productForm.valid) {
      this.isSaving = true;
      const productData = this.productForm.value;
      const editing = this.editingProduct();

      const obs = editing 
        ? this.productService.updateProduct(editing.id, productData)
        : this.productService.createProduct(productData);

      obs.subscribe({
        next: () => {
          this.isSaving = false;
          this.notificationService.success(`Product ${editing ? 'updated' : 'created'} successfully.`);
          this.closePanel();
          this.loadData();
        },
        error: (err) => {
          this.isSaving = false;
          this.notificationService.error(err.error?.message || 'Error saving product.');
        }
      });
    }
  }

  onRequestDelete(id: number) {
    this.productToDeleteId.set(id);
    this.isDeleteModalOpen.set(true);
    document.body.classList.add('scroll-lock');
  }

  confirmDelete() {
    const id = this.productToDeleteId();
    if (id !== null) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.notificationService.success('Product deleted.');
          this.loadData();
          this.closeDeleteModal();
        },
        error: () => {
          this.notificationService.error('Failed to delete product.');
          this.closeDeleteModal();
        }
      });
    }
  }

  closeDeleteModal() {
    this.isDeleteModalOpen.set(false);
    this.productToDeleteId.set(null);
    document.body.classList.remove('scroll-lock');
  }
}
