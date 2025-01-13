import {ViewConfig} from '@vaadin/hilla-file-router/types.js';
import {useSignal} from '@vaadin/hilla-react-signals';
import {Button, HorizontalLayout, Notification, TextField, VerticalLayout} from '@vaadin/react-components';
import {HelloWorldService} from 'Frontend/generated/endpoints.js';
import {useAuth} from "Frontend/util/auth";

export const config: ViewConfig = {
    menu: {order: 0},
    title: 'Hello World',
    loginRequired: true,
};

export default function HelloWorldView() {
    const name = useSignal('');
    const auth = useAuth();

    return (
        <VerticalLayout theme="spacing padding">
            <h1>Hello {auth.state.user?.username}!</h1>
            <form id="logout" action="/logout" method="post">
                <input type="hidden" name="_csrf" value={document.querySelector("meta[name='_csrf']")?.getAttribute("content") || ''}/>
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
                    }}
                >
                    Say hello
                </Button>
            </HorizontalLayout>
        </VerticalLayout>
    );
}
