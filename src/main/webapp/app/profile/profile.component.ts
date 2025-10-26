import { Component, OnInit, signal } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
})
export class ProfileComponent implements OnInit {
  loading = signal(true);
  success = signal(false);
  error = signal(false);

  userId: number | null = null;

  profileForm = new FormGroup({
    nom: new FormControl('', [Validators.required]),
    prenom: new FormControl('', [Validators.required]),
    email: new FormControl({ value: '', disabled: true }, [Validators.required, Validators.email]),
    motDePasse: new FormControl(''),
    role: new FormControl('NORMAL_USER', [Validators.required]),
  });

  constructor(
    private http: HttpClient,
    private appConfig: ApplicationConfigService,
    private accountService: AccountService,
  ) {}

  ngOnInit(): void {
    console.log('ProfileComponent initialized');

    this.accountService.identity().subscribe(account => {
      if (account?.email) {
        console.log('Current user from AccountService:', account);

        // Now call API to get full utilisateur info
        this.http.get<any>(this.appConfig.getEndpointFor(`api/account`)).subscribe({
          next: data => {
            console.log('Utilisateur API response:', data);
            this.userId = data.id;
            this.profileForm.patchValue({
              nom: data.nom,
              prenom: data.prenom,
              email: data.email,
              role: data.role?.nom || 'NORMAL_USER',
            });
            this.loading.set(false);
          },
          error: err => {
            console.error('Utilisateur API error:', err);
            this.error.set(true);
            this.loading.set(false);
          },
        });
      } else {
        console.error('No account information available');
        this.error.set(true);
        this.loading.set(false);
      }
    });
  }

  updateProfile(): void {
    if (!this.userId) return;

    const { nom, prenom, motDePasse, role } = this.profileForm.getRawValue();

    const payload = {
      id: this.userId,
      nom,
      prenom,
      email: this.profileForm.get('email')!.value,
      motDePasse: motDePasse || null,
      typeJustificatifAge: '',
      statut: '',
      role: { nom: role },
    };

    console.log('Update payload:', payload);

    this.http.put(this.appConfig.getEndpointFor(`api/utilisateurs/${this.userId}`), payload).subscribe({
      next: () => {
        console.log('Profile updated successfully');
        this.success.set(true);
        this.error.set(false);
      },
      error: err => {
        console.error('Update error:', err);
        this.error.set(true);
      },
    });
  }
}
