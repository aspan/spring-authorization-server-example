import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Signal, useSignal } from '@vaadin/hilla-react-signals';
import { Button, HorizontalLayout, Notification, TextField, VerticalLayout } from '@vaadin/react-components';
import { HelloWorldService, ResourceEndpointService } from 'Frontend/generated/endpoints.js';
import { useAuth } from 'Frontend/util/auth';
import { useEffect } from 'react';
import Resource from 'Frontend/generated/com/example/resource/client/Resource';

export const config: ViewConfig = {
  menu: { order: 0 },
  title: 'Hello World',
  loginRequired: true,
};

export default function HelloWorldView() {
  const name = useSignal('');
  const auth = useAuth();
  const resources: Signal<Resource[]> = useSignal([]);

  useEffect(() => {
    ResourceEndpointService.resources().then((value) => {
      resources.value = value;
    });
  }, []);

  return (
    <VerticalLayout theme="spacing padding">
      <h1>Hello {auth.state.user?.username}!</h1>
      <h2>Resources</h2>
      <ul>
        {resources.value.map((value, index) => (
          <li key={index}>{value.name}</li>
        ))}
      </ul>
      <a href="http://localhost:9000/webauthn/register">Register passkey</a>
      <form id="logout" action="/logout" method="post">
        <input
          type="hidden"
          name="_csrf"
          value={document.querySelector("meta[name='_csrf']")?.getAttribute('content') || ''}
        />
        <Button onClick={async () => (document.querySelector('#logout') as HTMLFormElement).submit()}>Logout</Button>
      </form>
      <HorizontalLayout theme="spacing" style={{ alignItems: 'end' }}>
        <TextField
          label="Your name"
          onValueChanged={(e) => {
            name.value = e.detail.value;
          }}
        />
        <Button
          onClick={async () => {
            const serverResponse = await HelloWorldService.sayHello(name.value);
            Notification.show(serverResponse);
          }}>
          Say hello
        </Button>
      </HorizontalLayout>
    </VerticalLayout>
  );
}
