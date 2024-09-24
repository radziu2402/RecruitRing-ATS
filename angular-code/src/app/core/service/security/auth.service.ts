import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {BehaviorSubject, Observable, shareReplay, Subject, tap, throwError} from 'rxjs';
import {jwtDecode} from "jwt-decode";
import dayjs from "dayjs";
import {environment} from "../../../../environments/environment";
import {MatSnackBar} from '@angular/material/snack-bar';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private _authStatus = new BehaviorSubject<boolean>(this.hasToken());
  public authStatus$ = this._authStatus.asObservable();
  private sessionEvents$ = new Subject<string>();

  private readonly loginUrl = environment.api + 'login';
  private keyToken = 'jwt_token';

  constructor(private http: HttpClient, private router: Router, private snackBar: MatSnackBar) {
  }

  login(login: string, password: string): Observable<any> {
    return this.http.post<Token>(this.loginUrl, {login: login, password: password})
      .pipe(
        tap((r: Token) => {
          this.setSession(r);
          this.showNotification('Zalogowano pomyślnie');
        }),
        shareReplay(1),
        catchError((error) => {
          this.showNotification('Błąd logowania: Nieprawidłowe dane');
          return throwError(() => new Error(error));
        })
      );
  }

  getAuthToken(): string | null {
    return localStorage.getItem('jwt_token');
  }

  getRole(): string | null {
    const tokenPayload = this.getTokenPayload();
    return tokenPayload ? tokenPayload.role : null; // Pobieramy rolę z tokenu
  }

  private hasToken(): boolean {
    return this.isLoggedIn();
  }

  showNotification(message: string) {
    this.snackBar.open(message, 'Zamknij', {duration: 3000});
  }

  logout() {
    localStorage.removeItem(this.keyToken);
    void this.router.navigate(["login"])
    this.showNotification('Wylogowano pomyślnie');
  }

  isLoggedIn() {
    const expirationDate = this.getTokenExpirationDate();
    return expirationDate?.isValid() && dayjs().isBefore(expirationDate);
  }

  private setSession(token: Token) {
    localStorage.setItem(this.keyToken, token.accessToken);
    this.sessionEvents$.next(token.accessToken);
  }

  private getTokenExpirationDate() {
    const tokenPayload = this.getTokenPayload();
    const expirationDate = tokenPayload?.exp;

    return dayjs.unix(+expirationDate);
  }

  private getTokenPayload(): any {
    const token = localStorage.getItem(this.keyToken);
    let tokenPayload: any;

    if (!token) {
      return;
    }

    try {
      tokenPayload = jwtDecode(token);
    } catch (error) {
      console.log('Not parsable JWT');
      localStorage.removeItem(this.keyToken);
    }

    return tokenPayload;
  }
}

type Token = { accessToken: string };
