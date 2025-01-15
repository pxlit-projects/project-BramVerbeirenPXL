//review.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private baseUrl = 'http://localhost:8085/review/api/reviews';

  constructor(private http: HttpClient) {}

  getPendingReviews(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/pending`);
  }

  approvePost(postId: number, data: { rejectionComment: string, approved: boolean }): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${postId}/approve`, data);
  }

  rejectPost(postId: number, data: { rejectionComment: string, approved: boolean }): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${postId}/reject`, data);
  }
}
