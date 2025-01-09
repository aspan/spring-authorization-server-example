import {ViewConfig} from '@vaadin/hilla-file-router/types.js';
import {Button, VerticalLayout} from '@vaadin/react-components';

export const config: ViewConfig = {
    menu: {exclude: true},
    flowLayout: false,
};

export default function LoginView() {

    return (
        <VerticalLayout theme="spacing padding"  style={{ alignItems: 'center' }}>
            <h1>Login</h1>
            <a href="/oauth2/authorization/hilla-client-oidc">Login</a>
        </VerticalLayout>
    );
}
