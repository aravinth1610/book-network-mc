import { Component } from '@angular/core';
import { Subscription } from 'rxjs';
import { BookService } from 'src/app/services/book/book.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css'],
})
export class BookComponent {
  constructor(private bookService: BookService) {}
  subscriptions: Subscription[] = [];
  bookResponseModal: any;

  ngOnInit(): void {
    this.subscriptions.push(
      this.bookService.books().subscribe({
        next: (pendingResponse: any) => {
          console.log(pendingResponse);
          this.bookResponseModal = pendingResponse;
        },
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((unsubscription) => {
      unsubscription.unsubscribe();
    });
  }
}
