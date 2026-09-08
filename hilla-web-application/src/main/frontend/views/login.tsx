import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, VerticalLayout } from '@vaadin/react-components';

export const config: ViewConfig = {
  menu: { exclude: true },
  flowLayout: false,
};

export default function LoginView() {
  return (
    <VerticalLayout theme="spacing padding" style={{ alignItems: 'center' }}>
      <h1>Login</h1>
      <form id="login" action="/oauth2/authorization/hilla-client" method="get">
        <Button onClick={async () => (document.querySelector('#login') as HTMLFormElement).submit()}>Login</Button>
      </form>
    </VerticalLayout>
  );
}
