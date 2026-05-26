import { useNavigate } from 'react-router-dom';
import { BiLogOutCircle } from "react-icons/bi";

function Logout() {
  const navigate = useNavigate();

  const logout = () => {
    sessionStorage.removeItem('nomeUsuario');
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('userProfile');
    sessionStorage.removeItem('classeId');
    sessionStorage.removeItem('nomePerfil');
    sessionStorage.removeItem('userPermissions');
    sessionStorage.removeItem('usuarioId');
    sessionStorage.removeItem('perfilId');
    sessionStorage.removeItem('igrejaId');
    navigate('/');
  };

  return (
      <BiLogOutCircle onClick={logout} className='bg-yellow-400 rounded-full w-10 h-10' title='logout'/>
    
  );
}

export default Logout;