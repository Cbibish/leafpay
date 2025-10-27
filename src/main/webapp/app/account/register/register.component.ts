import { AfterViewInit, Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/config/error.constants';
import SharedModule from 'app/shared/shared.module';
import PasswordStrengthBarComponent from '../password/password-strength-bar/password-strength-bar.component';
import { RegisterService } from './register.service';

type Role = 'PROFESSIONAL_USER' | 'STUDENT_USER' | 'USER';

@Component({
  selector: 'jhi-register',
  imports: [SharedModule, RouterModule, FormsModule, ReactiveFormsModule, PasswordStrengthBarComponent],
  templateUrl: './register.component.html',
})
export default class RegisterComponent implements AfterViewInit {
  doNotMatch = signal(false);
  error = signal(false);
  errorEmailExists = signal(false);
  errorUserExists = signal(false);
  success = signal(false);

  accountTypeToAdd = new FormControl('');

  roles = signal<string[]>([]);
  justificatifs = ['Passport', 'CNI', 'Autre chose'];

  registerForm: FormGroup;

  private readonly fb = inject(FormBuilder);
  private readonly translateService = inject(TranslateService);
  private readonly registerService = inject(RegisterService);

  private allowedAccountTypesByRole: Record<Role, string[]> = {
    PROFESSIONAL_USER: ['Professional', 'Savings'],
    STUDENT_USER: ['Student', 'Savings'],
    USER: ['Normal', 'Savings'],
  };

  constructor() {
    this.registerForm = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      motDePasse: ['', [Validators.required, Validators.minLength(4)]],
      confirmMotDePasse: ['', Validators.required],
      role: ['NORMAL_USER', Validators.required],
      dateNaissance: ['', Validators.required],
      typeJustificatifAge: ['', Validators.required],
      comptes: this.fb.array([]),
    });
  }

  ngAfterViewInit(): void {
    this.loadRoles();
    this.registerForm.get('role')?.valueChanges.subscribe(() => {
      this.comptes.clear();
      this.accountTypeToAdd.setValue('');
    });
  }

  get comptes(): FormArray {
    return this.registerForm.get('comptes') as FormArray;
  }

  getAvailableAccountTypes(index?: number): string[] {
    const role = this.registerForm.get('role')?.value as Role;
    const allowedTypes = this.allowedAccountTypesByRole[role] || [];

    const selectedTypes = this.comptes.controls
      .map((ctrl, i) => (i === index ? null : ctrl.get('typeCompte')?.value))
      .filter((type): type is string => !!type);

    return allowedTypes.filter(type => !selectedTypes.includes(type));
  }

  addCompte(type: string): void {
    if (!type) return;

    const role = this.registerForm.get('role')?.value as Role;
    const nowISO = new Date().toISOString();

    const plafondTransaction = role === 'STUDENT_USER' ? 500 : 1_000_000;
    let limiteRetraitsMensuels = 1_000_000;

    if (role === 'STUDENT_USER' || ['savings', 'épargne'].includes(type.toLowerCase())) {
      limiteRetraitsMensuels = 5;
    }

    const tauxInteret = 0.03;

    const compteGroup = this.fb.group({
      typeCompte: [type, Validators.required],
      solde: [0, [Validators.required, Validators.min(0)]],
      plafondTransaction: [plafondTransaction, Validators.required],
      limiteRetraitsMensuels: [limiteRetraitsMensuels, Validators.required],
      tauxInteret: [tauxInteret, Validators.required],
      dateOuverture: [nowISO, Validators.required],
      statut: ['active', Validators.required],
      iban: [this.generateRandomIBAN(), Validators.required],
    });

    this.comptes.push(compteGroup);
    this.accountTypeToAdd.setValue('');
  }

  removeCompte(index: number): void {
    this.comptes.removeAt(index);
  }

  generateRandomIBAN(): string {
    const countryCode = 'FR';
    const checkDigits = '76';
    const bankCode = '30006';
    const branchCode = '00001';
    const accountNumber = Math.floor(Math.random() * 1e11)
      .toString()
      .padStart(11, '0');
    const ribKey = Math.floor(Math.random() * 100)
      .toString()
      .padStart(2, '0');
    return `${countryCode}${checkDigits}${bankCode}${branchCode}${accountNumber}${ribKey}`;
  }

  loadRoles(): void {
    this.registerService.getRoles().subscribe((roles: any[]) => {
      const backendToFrontendMap: Record<string, Role> = {
        USER: 'USER',
        PROFESSIONAL_USER: 'PROFESSIONAL_USER',
        STUDENT_USER: 'STUDENT_USER',
      };

      const mappedRoles = roles.map(r => backendToFrontendMap[r.nom]).filter((nom): nom is Role => !!nom);

      this.roles.set(mappedRoles);
    });
  }

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

    const registrationPayload = this.preparePayload();

    this.registerService.save(registrationPayload).subscribe({
      next: () => this.success.set(true),
      error: (response: HttpErrorResponse) => this.processError(response),
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

  preparePayload() {
    const formValue = this.registerForm.getRawValue();

    const roleIdMap: Record<Role, string> = {
      USER: '1501',
      PROFESSIONAL_USER: '1502',
      STUDENT_USER: '1505',
    };
    const roleName = formValue.role as Role;

    const rolePayload = {
      id: roleIdMap[roleName] || '1000',
      nom: roleName,
    };

    const comptes = formValue.comptes.map((compte: any) => ({
      typeCompte: compte.typeCompte,
      solde: compte.solde,
      plafondTransaction: compte.plafondTransaction,
      limiteRetraitsMensuels: compte.limiteRetraitsMensuels,
      tauxInteret: compte.tauxInteret,
      dateOuverture: compte.dateOuverture,
      statut: compte.statut,
      iban: compte.iban,
    }));

    return {
      nom: formValue.nom,
      prenom: formValue.prenom,
      email: formValue.email,
      motDePasse: formValue.motDePasse,
      dateNaissance: formValue.dateNaissance || '2000-01-01',
      typeJustificatifAge: formValue.typeJustificatifAge || 'Passport',
      statut: 'active',
      role: rolePayload,
      comptes,
    };
  }

  isStudent(compte: AbstractControl): boolean {
    return compte.get('typeCompte')?.value === 'Student';
  }

  isSaving(compte: AbstractControl): boolean {
    return compte.get('typeCompte')?.value === 'Savings';
  }
}
