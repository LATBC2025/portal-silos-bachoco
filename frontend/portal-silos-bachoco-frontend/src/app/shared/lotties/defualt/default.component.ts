import { AfterViewInit, Component, Input, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { LottieComponent } from 'ngx-lottie';
import { AnimationItem, AnimationOptions } from 'ngx-lottie/lib/symbols';

@Component({
    selector: 'app-lottie-default',
    standalone: true,
    imports: [LottieComponent],
    template: `
    <ng-lottie
      #lottieEl
      [options]="options"
      (mouseenter)="playAnimation()"
      (mouseleave)="stopAnimation()">
    </ng-lottie>
  `,
    styles: `
     :host {
        display: block;
        cursor: pointer;
      }
  `
})
export class DefaultComponent implements OnInit, OnChanges, AfterViewInit {
  @Input() lottie!: string;

  @ViewChild('lottieEl') lottieEl!: LottieComponent;
  private animation: AnimationItem | null = null;

  options: AnimationOptions = {
    path: '',
    loop: false,
    autoplay: false,
  };

  constructor() {
  }

  ngOnInit(): void {
    this.updateOptions();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['lottie']) {
      this.updateOptions();
    }
  }

  ngAfterViewInit(): void {
    if (this.lottieEl) {
      this.lottieEl.animationCreated.subscribe((anim: AnimationItem) => {
        this.animation = anim;
      });
    }
  }

  private updateOptions(): void {
    if (this.lottie) {
      this.options = {
        path: `assets/lotties/${this.lottie}.json`,
        loop: false,
        autoplay: false,
      };
    }
  }

  playAnimation(): void {
    this.animation?.play();
  }

  stopAnimation(): void {
    this.animation?.stop();
  }
}
