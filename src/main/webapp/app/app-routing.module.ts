import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute } from './layouts';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { ProductorsListComponent } from 'app/productors-list/productors-list.component';
import { CommandsListComponent } from 'app/commands-list/commands-list.component';
import { CommandCoffeeComponent } from 'app/command-coffee/command-coffee.component';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                {
                    path: 'admin',
                    loadChildren: './admin/admin.module#LensleauAdminModule'
                },
                {
                    path: 'productors',
                    component: ProductorsListComponent
                },
                {
                    path: 'commands',
                    component: CommandsListComponent
                },
                {
                    path: 'command',
                    component: CommandCoffeeComponent
                },
                ...LAYOUT_ROUTES
            ],
            { useHash: true, enableTracing: DEBUG_INFO_ENABLED }
        )
    ],
    exports: [RouterModule]
})
export class LensleauAppRoutingModule {}
