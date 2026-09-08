import { configureAuth } from '@vaadin/hilla-react-auth';
import { UserService } from 'Frontend/generated/endpoints';

const auth = configureAuth(UserService.getAuthenticatedUser);

export const useAuth = auth.useAuth;
export const AuthProvider = auth.AuthProvider;
