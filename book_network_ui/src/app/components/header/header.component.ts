import { Component } from '@angular/core';
import { HeaderConstant } from 'src/app/constants/header-constant';

@Component({
  selector: 'header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent {

  menus: any[] = [];

  constructor() {
    HeaderConstant.menus.forEach((menuRoles) => {
      // const isRolePresent = menuRoles.roles.find(
      //   (fr) => authStorageServices.getAuthRole().includes(fr) //authStorageServices.getAuthRole()
      // );
//      if (isRolePresent) {
        this.menus.push(menuRoles);
//      }
    });
  }
}
