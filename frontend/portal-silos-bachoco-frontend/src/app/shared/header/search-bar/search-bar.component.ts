import { Component, ElementRef, ViewChild } from '@angular/core';

import { MENU } from '../../aside/aside-menu/menu.list';
import { Router } from '@angular/router';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';

@Component({
    selector: 'app-search-bar',
    standalone: true,
    imports: [
        FormsModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatIconModule,
    ],
    template: `
    <!-- <mat-form-field class="w-100">
      <mat-icon matPrefix>search</mat-icon>
      <mat-label>Buscar</mat-label>
      <input #input
            type="text"
            placeholder="¿Qué deseas buscar?"
            matInput
            [formControl]="myControl"
            [matAutocomplete]="auto"
            (input)="filter()"
            (focus)="filter()">
      <mat-autocomplete requireSelection #auto="matAutocomplete">
        @for (option of filteredOptions; track option) {
          <mat-option [value]="option.name" (click)="redirectTo(option.name)">{{option.name}}</mat-option>
        }
      </mat-autocomplete>
    </mat-form-field> -->
  `,
    styles: `
    :host {
      display: block;
      width: 100%;
    }
    :host::ng-deep .mat-mdc-form-field-subscript-wrapper {
      display: none;
    }

    :host::ng-deep ::ng-deep .mat-mdc-text-field-wrapper {
      border-radius: 5rem!important;
    }
  `
})
export class SearchBarComponent {
  @ViewChild('input') input!: ElementRef<HTMLInputElement>;
  myControl = new FormControl('');
  options: any[];
  filteredOptions: any[];

  constructor(protected router: Router) {
    this.options = MENU.reduce((acc, menu) => {
      acc.push({ name: menu.name, url: menu.url });
      menu.children?.forEach(child => acc.push({ name: child.name, url: child.url }));
      return acc;
    }, [] as { name: string; url: string }[]);
    this.filteredOptions = this.options ? this.options.slice() : [];
  }

  filter(): void {
    const value = this.input.nativeElement.value;
    if (value) {
      this.filteredOptions = this.options.filter(option => option.name.toLowerCase().includes(value.toLowerCase()));
    } else {
      this.filteredOptions = this.options.slice();
    }
  }

  redirectTo(option: string): void {
    console.log(option);
    const url = this.options.find(o => o.name === option)?.url;
    if (url && url.startsWith('http')) {
      window.open(url, '_blank');
      return;
    }
    if (url) {
      this.router.navigate([url]);
    }
  }
}
