import { Navigate } from 'react-router-dom';

type PrivateRouteProps = {
  children: JSX.Element;
  allowedProfiles: string[];
};


const PrivateRoute = ({ children, allowedProfiles }: PrivateRouteProps) => {
  const accessToken = sessionStorage.getItem('accessToken');
  const isAuthenticated = !!accessToken;

  // Supondo que o perfil do usuário está armazenado em sessionStorage após login
  const userProfile = sessionStorage.getItem('userProfile');

  // Verifica se o usuário está autenticado e se seu perfil está entre os permitidos
  if (!isAuthenticated) {
    return <Navigate to="/" />;
  }

  if (allowedProfiles && !allowedProfiles.includes(userProfile!)) {
    return <Navigate to="/home" />; // Redireciona se o perfil não for permitido
  }

  return children;
};

export default PrivateRoute;