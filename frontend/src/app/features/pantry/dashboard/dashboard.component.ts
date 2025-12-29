import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { ProductService } from '../../../core/services/product.service';
import { Product, Category } from '../../../core/models/pantry.model';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { CardComponent } from '../../../shared/components/card/card.component';
import { InputComponent } from '../../../shared/components/input/input.component';
import { SidePanelComponent } from '../../../shared/components/side-panel/side-panel.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    ButtonComponent, 
    CardComponent, 
    InputComponent, 
    SidePanelComponent
  ],
  template: `
    <div class="dashboard-layout">
      <nav class="navbar">
        <div class="brand">SmartPantry</div>
        <div class="user-info">
          <span>{{ authService.currentUser()?.username }}</span>
          <div class="avatar">{{ authService.currentUser()?.username?.[0]?.toUpperCase() }}</div>
          <sp-button variant="ghost" (click)="authService.logout()">Logout</sp-button>
        </div>
      </nav>
      
      <main class="content">
        <header class="content-header">
          <div>
            <h1>Inventory Overview</h1>
            <p>Manage your products and track expiry dates.</p>
          </div>
          <sp-button (click)="openCreatePanel()">
            <span>+</span> Add Product
          </sp-button>
        </header>

        <div class="stats-grid">
          <sp-card class="stat-card">
            <span class="stat-label">Total Items</span>
            <span class="stat-value">{{ products().length }}</span>
          </sp-card>
          <sp-card class="stat-card">
            <span class="stat-label">Expiring Soon</span>
            <span class="stat-value warning">{{ expiringSoonCount() }}</span>
          </sp-card>
        </div>

        <sp-card title="Recent Products" [noPadding]="true">
          <table class="data-table">
            <thead>
              <tr>
                <th>Product Name</th>
                <th>Category</th>
                <th>Quantity</th>
                <th>Expiry Date</th>
                <th>Status</th>
                <th class="actions"></th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let product of products()">
                <td><strong>{{ product.name }}</strong></td>
                <td><span class="category-tag">{{ product.categoryName }}</span></td>
                <td>{{ product.quantity }}</td>
                <td>{{ product.expirationDate | date:'mediumDate' }}</td>
                <td>
                  <span [class]="'status-badge ' + product.expiryStatus.toLowerCase()">
                    {{ product.expiryStatus }}
                  </span>
                </td>
                <td class="actions">
                  <button class="icon-btn" (click)="openEditPanel(product)">Edit</button>
                  <button class="icon-btn delete" (click)="deleteProduct(product.id)">Delete</button>
                </td>
              </tr>
              <tr *ngIf="products().length === 0">
                <td colspan="6" class="empty-state">No products found. Add your first item!</td>
              </tr>
            </tbody>
          </table>
        </sp-card>
      </main>

      <sp-side-panel 
        [isOpen]="isPanelOpen()" 
        [title]="editingProduct() ? 'Edit Product' : 'Add New Product'"
        (onClose)="closePanel()">
        
        <form [formGroup]="productForm" (ngSubmit)="saveProduct()">
          <sp-input
            label="Product Name"
            placeholder="e.g. Milk"
            formControlName="name"
            [error]="productForm.get('name')?.touched && productForm.get('name')?.invalid ? 'Name is required' : ''"
          ></sp-input>

          <div class="form-row">
            <sp-input
              label="Quantity"
              type="number"
              placeholder="0"
              formControlName="quantity"
            ></sp-input>
            
            <div class="input-container">
              <label>Category</label>
              <div class="category-input-group">
                <select formControlName="categoryId" class="select-field">
                  <option *ngFor="let cat of categories()" [value]="cat.id">{{ cat.name }}</option>
                </select>
                <button type="button" class="add-cat-btn" (click)="toggleNewCategory()">+</button>
              </div>
            </div>
          </div>

          <div class="new-category-inline" *ngIf="isAddingCategory()">
            <sp-input
              #newCatInput
              label="New Category Name"
              placeholder="e.g. Dairy"
              [value]="newCategoryName()"
              (input)="updateNewCategoryName($event)"
            ></sp-input>
            <div class="new-cat-actions">
              <sp-button variant="ghost" type="button" (click)="toggleNewCategory()">Cancel</sp-button>
              <sp-button type="button" (click)="saveCategory()" [disabled]="!newCategoryName() || isSavingCategory">
                Add
              </sp-button>
            </div>
          </div>

          <sp-input
            label="Expiry Date"
            type="date"
            formControlName="expirationDate"
            [error]="productForm.get('expirationDate')?.touched && productForm.get('expirationDate')?.invalid ? 'Valid date is required' : ''"
          ></sp-input>

          <div class="panel-actions">
            <sp-button variant="ghost" type="button" (click)="closePanel()">Cancel</sp-button>
            <sp-button type="submit" [disabled]="productForm.invalid || isSaving">
              {{ isSaving ? 'Saving...' : (editingProduct() ? 'Update Product' : 'Add Product') }}
            </sp-button>
          </div>
        </form>
      </sp-side-panel>
    </div>
  `,
  styles: [`
    .dashboard-layout {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      background-color: var(--bg-surface);
    }

    .navbar {
      height: 64px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 var(--spacing-xl);
      background: white;
      border-bottom: 1px solid var(--border-color);
    }

    .brand {
      font-weight: 700;
      color: var(--primary);
      font-size: 1.25rem;
      letter-spacing: -0.025em;
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: var(--spacing-md);
    }

    .avatar {
      width: 32px;
      height: 32px;
      background: var(--primary-light);
      color: var(--primary);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 600;
      font-size: 0.75rem;
    }

    .content {
      flex: 1;
      padding: var(--spacing-xl);
      max-width: 1200px;
      margin: 0 auto;
      width: 100%;
    }

    .content-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: var(--spacing-xl);
    }

    .content-header h1 {
      font-size: 1.875rem;
      font-weight: 700;
      color: var(--text-main);
      letter-spacing: -0.025em;
    }

    .content-header p {
      color: var(--text-muted);
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: var(--spacing-md);
      margin-bottom: var(--spacing-xl);
    }

    .stat-card {
      display: flex;
      flex-direction: column;
    }

    .stat-label {
      font-size: 0.75rem;
      font-weight: 600;
      color: var(--text-muted);
      text-transform: uppercase;
      letter-spacing: 0.05em;
    }

    .stat-value {
      font-size: 2rem;
      font-weight: 700;
      color: var(--text-main);
    }

    .stat-value.warning {
      color: var(--warning);
    }

    .data-table {
      width: 100%;
      border-collapse: collapse;
      text-align: left;
    }

    .data-table th {
      padding: var(--spacing-md) var(--spacing-lg);
      font-size: 0.75rem;
      font-weight: 600;
      color: var(--text-muted);
      text-transform: uppercase;
      border-bottom: 1px solid var(--border-color);
      background-color: var(--bg-surface);
    }

    .data-table td {
      padding: var(--spacing-md) var(--spacing-lg);
      border-bottom: 1px solid var(--border-color);
      font-size: 0.875rem;
    }

    .category-tag {
      padding: 0.25rem 0.5rem;
      background: var(--bg-surface);
      border: 1px solid var(--border-color);
      border-radius: 4px;
      font-size: 0.75rem;
      color: var(--text-muted);
    }

    .status-badge {
      padding: 0.25rem 0.75rem;
      border-radius: 99px;
      font-size: 0.75rem;
      font-weight: 600;
      text-transform: capitalize;
    }

    .status-badge.green { background: #dcfce7; color: #166534; }
    .status-badge.yellow { background: #fef9c3; color: #854d0e; }
    .status-badge.red { background: #fee2e2; color: #991b1b; }

    .actions {
      text-align: right;
      display: flex;
      gap: var(--spacing-md);
      justify-content: flex-end;
    }

    .icon-btn {
      font-size: 0.75rem;
      font-weight: 600;
      color: var(--primary);
    }

    .icon-btn.delete {
      color: var(--error);
    }

    .empty-state {
      text-align: center;
      padding: var(--spacing-xl) !important;
      color: var(--text-muted);
    }

    .form-row {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: var(--spacing-md);
    }

    .input-container {
      display: flex;
      flex-direction: column;
      gap: 0.375rem;
      margin-bottom: 1rem;
    }

    .input-container label {
      font-size: 0.75rem;
      font-weight: 600;
      color: var(--text-muted);
      text-transform: uppercase;
    }

    .select-field {
      padding: 0.625rem 0.875rem;
      border: 1px solid var(--border-color);
      border-radius: 4px;
      font-size: 0.875rem;
      background-color: white;
      flex: 1;
    }

    .category-input-group {
      display: flex;
      gap: var(--spacing-xs);
    }

    .add-cat-btn {
      width: 40px;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: var(--primary-light);
      color: var(--primary);
      border-radius: 4px;
      font-size: 1.25rem;
      font-weight: 600;
      transition: all 0.2s;
    }

    .add-cat-btn:hover {
      background: var(--primary);
      color: white;
    }

    .new-category-inline {
      margin-top: -var(--spacing-md);
      margin-bottom: var(--spacing-md);
      padding: var(--spacing-md);
      background: var(--bg-surface);
      border-radius: 8px;
      border: 1px dashed var(--border-color);
    }

    .new-cat-actions {
      display: flex;
      gap: var(--spacing-sm);
      justify-content: flex-end;
    }

    .new-cat-actions sp-button {
      flex: 0 1 auto;
    }

    ::ng-deep .new-cat-actions button {
      padding: 0.4rem 0.8rem;
      font-size: 0.75rem;
    }

    .panel-actions {
      display: flex;
      gap: var(--spacing-md);
      margin-top: var(--spacing-xl);
    }

    .panel-actions sp-button {
      flex: 1;
    }

    ::ng-deep .panel-actions button {
      width: 100%;
    }
  `]
})
export class DashboardComponent implements OnInit {
  products = signal<Product[]>([]);
  categories = signal<Category[]>([]);
  expiringSoonCount = signal(0);
  
  isPanelOpen = signal(false);
  isSaving = false;
  editingProduct = signal<Product | null>(null);

  isAddingCategory = signal(false);
  newCategoryName = signal('');
  isSavingCategory = false;
  
  productForm: FormGroup;

  constructor(
    public authService: AuthService,
    private productService: ProductService,
    private fb: FormBuilder
  ) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      expirationDate: ['', Validators.required],
      categoryId: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.productService.getProducts().subscribe(products => {
      this.products.set(products);
      this.expiringSoonCount.set(products.filter(p => p.expiryStatus !== 'GREEN').length);
    });
    this.productService.getCategories().subscribe(categories => {
      this.categories.set(categories);
      if (categories.length > 0 && !this.productForm.get('categoryId')?.value) {
        this.productForm.patchValue({ categoryId: categories[0].id });
      }
    });
  }

  openCreatePanel() {
    this.editingProduct.set(null);
    this.productForm.reset({
      quantity: 1,
      categoryId: this.categories()[0]?.id
    });
    this.isPanelOpen.set(true);
  }

  openEditPanel(product: Product) {
    this.editingProduct.set(product);
    this.productForm.patchValue({
      name: product.name,
      quantity: product.quantity,
      expirationDate: product.expirationDate.split('T')[0],
      categoryId: product.categoryId
    });
    this.isPanelOpen.set(true);
  }

  closePanel() {
    this.isPanelOpen.set(false);
    this.isAddingCategory.set(false);
    this.newCategoryName.set('');
  }

  toggleNewCategory() {
    this.isAddingCategory.set(!this.isAddingCategory());
    this.newCategoryName.set('');
  }

  updateNewCategoryName(event: any) {
    this.newCategoryName.set(event.target.value);
  }

  saveCategory() {
    const name = this.newCategoryName().trim();
    if (name) {
      this.isSavingCategory = true;
      this.productService.createCategory({ name }).subscribe({
        next: (newCat) => {
          this.isSavingCategory = false;
          this.isAddingCategory.set(false);
          this.newCategoryName.set('');
          
          // Refresh categories and select the new one
          this.productService.getCategories().subscribe(categories => {
            this.categories.set(categories);
            this.productForm.patchValue({ categoryId: newCat.id });
          });
        },
        error: () => {
          this.isSavingCategory = false;
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
          this.closePanel();
          this.loadData();
        },
        error: () => this.isSaving = false
      });
    }
  }

  deleteProduct(id: number) {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe(() => this.loadData());
    }
  }
}
