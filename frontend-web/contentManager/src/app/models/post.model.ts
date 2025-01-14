//post.model.ts
export interface Post {
  id: number;
  title: string;
  content: string;
  author: string;
  authorEmail: string;
  draft: boolean;
  approved: boolean;
  rejectionComment?: string;
  createdDate?: Date;
}
