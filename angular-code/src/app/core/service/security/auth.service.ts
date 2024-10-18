import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {BehaviorSubject, Observable, shareReplay, Subject, tap, throwError} from 'rxjs';
import {jwtDecode} from "jwt-decode";
import dayjs from 'dayjs/esm';
import {environment} from "../../../../environments/environment";
import {MatSnackBar} from '@angular/material/snack-bar';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly _authStatus = new BehaviorSubject<boolean>(false);
  public authStatus$ = this._authStatus.asObservable();
  private readonly sessionEvents$ = new Subject<string>();

  private readonly loginUrl = environment.api + 'login';
  private readonly keyToken = 'jwt_token';

  constructor(private readonly http: HttpClient, private readonly router: Router, private readonly snackBar: MatSnackBar) {
    this._authStatus.next(this.isLoggedIn());
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

  resetPassword(login: string): Observable<any> {
    const resetPasswordUrl = environment.api + 'reset-password';

    const headers = {'X-Skip-Spinner': 'true'};

    return this.http.post(resetPasswordUrl, {login}, {headers})
      .pipe(
        catchError((error) => {
          return throwError(() => new Error(error));
        })
      );
  }

  sendVerificationCode(email: string): Observable<any> {
    const verificationUrl = environment.api + 'verify-email';
    const headers = {'X-Skip-Spinner': 'true'};
    return this.http.post(verificationUrl, {}, {
      params: {email},
      responseType: 'text',
      headers
    });
  }

  confirmVerificationCode(email: string, code: string): Observable<any> {
    const confirmUrl = environment.api + 'confirm-code';
    return this.http.post(confirmUrl, {}, { params: { email, code } });
  }

  confirmResetPassword(token: string, newPassword: string): Observable<any> {
    const confirmResetPasswordUrl = environment.api + `reset-password/confirm`;
    return this.http.post(confirmResetPasswordUrl, {token, newPassword});
  }

  verifyResetToken(token: string): Observable<boolean> {
    const verifyUrl = environment.api + `reset-password/verify-token?token=${token}`;
    return this.http.get<boolean>(verifyUrl);
  }

  getAuthToken(): string | null {
    return localStorage.getItem('jwt_token');
  }

  getRole(): string | null {
    const tokenPayload = this.getTokenPayload();
    return tokenPayload ? tokenPayload.role : null;
  }

  showNotification(message: string) {
    this.snackBar.open(message, 'Zamknij', {duration: 3000});
  }

  logout() {
    localStorage.removeItem(this.keyToken);
    void this.router.navigate(["login"])
    this.showNotification('Wylogowano pomyślnie');
    this._authStatus.next(false);
  }

  isLoggedIn() {
    const expirationDate = this.getTokenExpirationDate();
    return expirationDate?.isValid() && dayjs().isBefore(expirationDate);
  }

  private setSession(token: Token) {
    localStorage.setItem(this.keyToken, token.accessToken);
    this.sessionEvents$.next(token.accessToken);
    this._authStatus.next(true)
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
