export interface Category {
  id: number;
  name: string;
}

export interface Product {
  id: number;
  name: string;
  quantity: number;
  expiryDate: string;
  category: Category;
  expiryStatus: 'GREEN' | 'YELLOW' | 'RED';
}

export interface ProductRequest {
  name: string;
  quantity: number;
  expiryDate: string;
  categoryId: number;
}
