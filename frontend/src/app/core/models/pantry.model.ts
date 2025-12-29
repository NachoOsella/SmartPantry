export interface Category {
  id: number;
  name: string;
}

export interface CategoryRequest {
  name: string;
}

export interface Product {
  id: number;
  name: string;
  quantity: number;
  expirationDate: string;
  categoryName: string;
  categoryId: number;
  expiryStatus: 'GREEN' | 'YELLOW' | 'RED';
  daysRemaining: number;
}

export interface ProductRequest {
  name: string;
  quantity: number;
  expirationDate: string;
  categoryId: number;
}
