import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-rejection-dialog',
  template: `
    <h2>Afwijzen</h2>
    <textarea [(ngModel)]="comment" placeholder="Reden voor afwijzing"></textarea>
    <button mat-button (click)="submit()">Bevestigen</button>
    <button mat-button (click)="close()">Annuleren</button>
  `,
  imports: [CommonModule, FormsModule]
})
export class RejectionDialogComponent {
  comment = '';

  constructor(
    public dialogRef: MatDialogRef<RejectionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  submit() {
    this.dialogRef.close(this.comment);
  }

  close() {
    this.dialogRef.close();
  }
}
