import { AfterViewInit, Component, ElementRef, OnInit, ViewChild, inject, signal } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'jhi-login',
  standalone: true,
  imports: [SharedModule, FormsModule, ReactiveFormsModule, RouterModule, TranslateModule],
  templateUrl: './login.component.html',
})
export default class LoginComponent implements OnInit, AfterViewInit {
  @ViewChild('username', { static: false }) usernameInput!: ElementRef<HTMLInputElement>;

  authenticationError = signal(false);

  loginForm = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    rememberMe: new FormControl(false, { nonNullable: true }),
  });

  private readonly accountService = inject(AccountService);
  private readonly loginService = inject(LoginService);
  private readonly router = inject(Router);

  ngOnInit(): void {
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']);
      }
    });
  }

  ngAfterViewInit(): void {
    this.usernameInput?.nativeElement.focus();
  }

  protected onLoginSuccess(): void {
    this.authenticationError.set(false);

    this.accountService.identity(true).subscribe(account => {
      if (!account) {
        this.router.navigate(['/']);
        return;
      }

      // Use your custom role name from 'role.nom'
      const userRole = account.role?.nom ?? '';

      if (userRole === 'ADMIN' || userRole === 'CONSEILLER') {
        this.router.navigate(['/admin/admin-dashboard']);
      } else if (userRole === 'NORMAL_USER' || userRole === 'PROFESSIONAL_USER' || userRole === 'STUDENT_USER') {
        this.router.navigate(['/client-dashboard']);
      } else {
        this.router.navigate(['/']); // fallback
      }
    });
  }

  login(): void {
    if (this.loginForm.invalid) {
      return;
    }

    const { username, password, rememberMe } = this.loginForm.value;

    this.loginService
      .login({
        username: username!,
        password: password!,
        rememberMe: rememberMe ?? false,
      })
      .subscribe({
        next: () => this.onLoginSuccess(),
        error: () => this.authenticationError.set(true),
      });
  }
}
