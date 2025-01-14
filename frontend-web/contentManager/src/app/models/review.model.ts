//review.model.ts
export interface Review {
  id: number;
  postId: number;
  title: string;
  content: string;
  author: string;
  rejectionComment?: string;
  approved: boolean;
}
