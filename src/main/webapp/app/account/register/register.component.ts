import { AfterViewInit, Component, ElementRef, inject, signal, viewChild } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/config/error.constants';
import SharedModule from 'app/shared/shared.module';
import PasswordStrengthBarComponent from '../password/password-strength-bar/password-strength-bar.component';
import { RegisterService } from './register.service';
import { Registration } from './register.model';

@Component({
  selector: 'jhi-register',
  imports: [SharedModule, RouterModule, FormsModule, ReactiveFormsModule, PasswordStrengthBarComponent],
  templateUrl: './register.component.html',
})
export default class RegisterComponent implements AfterViewInit {
  // Removed old `login` field as it's not used anymore
  doNotMatch = signal(false);
  error = signal(false);
  errorEmailExists = signal(false);
  errorUserExists = signal(false);
  success = signal(false);

  registerForm = new FormGroup({
    nom: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    prenom: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
    motDePasse: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(4)] }),
    confirmMotDePasse: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    role: new FormControl('NORMAL_USER', { nonNullable: true }), // Default role
  });

  private readonly translateService = inject(TranslateService);
  private readonly registerService = inject(RegisterService);

  ngAfterViewInit(): void {}

  register(): void {
    this.doNotMatch.set(false);
    this.error.set(false);
    this.errorEmailExists.set(false);
    this.errorUserExists.set(false);

    const { motDePasse, confirmMotDePasse } = this.registerForm.getRawValue();
    if (motDePasse !== confirmMotDePasse) {
      this.doNotMatch.set(true);
      return;
    }

    const { nom, prenom, email, role } = this.registerForm.getRawValue();

    const registrationPayload: Registration = {
      nom,
      prenom,
      email,
      motDePasse,
      typeJustificatifAge: '', // removed from UI, keep empty

      role: { nom: role },
    };

    this.registerService.save(registrationPayload).subscribe({
      next: () => this.success.set(true),
      error: response => this.processError(response),
    });
  }

  private processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists.set(true);
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists.set(true);
    } else {
      this.error.set(true);
    }
  }
}
