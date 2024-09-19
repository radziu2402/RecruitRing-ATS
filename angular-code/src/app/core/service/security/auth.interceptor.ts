import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthInterceptor implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.url.endsWith('login')) {
      return next.handle(request);
    }
    const authToken = localStorage.getItem('jwt_token');

    const authReq = request.clone({
      headers: request.headers.set('Authorization', `Bearer ${authToken}`)
    });

    return next.handle(authReq);
  }
}
