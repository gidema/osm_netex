import { Component } from '@angular/core';

import { RouterLink, Router, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MainMenuComponent } from './main-menu/main-menu.component';
//import { HttpClientModule} from '@angular/common/http';

@Component({
    selector: 'app-root',
    imports: [RouterOutlet, MainMenuComponent, RouterLink, RouterLinkActive],
    templateUrl: './app.component.html',
    styleUrl: './app.component.css'
})
export class AppComponent {
    title = 'haltedata';

//  constructor(private router:Router) {
//  }
}
