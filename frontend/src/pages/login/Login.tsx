/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState } from "react";
import { FaConnectdevelop } from 'react-icons/fa';
import { useNavigate } from "react-router-dom";
import "./styles.css";
import api from "../../service/api";
import Header from "../../components/header/header";
import Footer from "../../components/footer/footer";
import { FaEye, FaEyeSlash } from "react-icons/fa";




function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const navigate = useNavigate();

  async function login(e: any) {
    e.preventDefault();
    if (!username.trim() || !password.trim()) {
      setError("Email e senha são obrigatórios!");
      return;
    }
    
    const data = { username, password };
    try {
      const response = await api.post('api/auth/login', data);
      const { jwt: token, classeId, role, perfilId, igrejaId, usuarioId, nomePerfil, userProfile, userPermissions } = response.data;
      sessionStorage.setItem('nomeUsuario', response.data.nomeUsuario);
      sessionStorage.setItem('accessToken', token);
      sessionStorage.setItem('userRole', role);
      sessionStorage.setItem('usuarioId', usuarioId);
      sessionStorage.setItem('perfilId', perfilId);
      sessionStorage.setItem('classeId', classeId);
      sessionStorage.setItem('igrejaId', igrejaId);
      sessionStorage.setItem('userProfile', userProfile);
      sessionStorage.setItem('userPermissions', userPermissions);
      sessionStorage.setItem('nomePerfil', nomePerfil);
      //console.log('Username:', username);
      //console.log('UserProfile:', userProfile);
      //console.log('UserPermissions:', userPermissions);
      //console.log('Nome do Perfil:', nomePerfil);
      navigate('/home');
    } catch (error: any) {
      if (error.response) {
        console.error('Erro de resposta:', error.response.data);
        if (error.response.status === 403) {
          setError('Acesso negado: Verifique suas credenciais!');
        } else {
          setError('Erro ao tentar fazer login. Tente novamente.');
        }
      } else if (error.request) {
        console.error('Erro de requisição:', error.request);
        setError('Erro de rede. Verifique sua conexão.');
      } else {
        console.error('Erro desconhecido:', error.message);
        setError('Erro ao tentar fazer login. Tente novamente.');
      }
      clearErrorAfterTimeout();
   }
  }

  function clearErrorAfterTimeout() {
    setTimeout(() => {
      setError("");
    }, 8000);
  }

  return (
      <>
      <header>
        <Header hideWelcome={true}/>
      </header>
    <div className="container">
      <div className="container-login">
        <div className="wrap-login">
          <form className="login-form" onSubmit={login}>
            <span className="login-form-title"> Bem vindo </span>

            <span className="login-form-title">
              <FaConnectdevelop title="Project" color="#00008B" className="w-24 h-24 ml-20 mt-5 mb-5" />
            </span>

            <div className="wrap-input">
              <input
                className={username !== "" ? "has-val input" : "input"}
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
              <span className="focus-input" data-placeholder="Email"></span>
            </div>

             <div className="wrap-input relative">
                <input
                  className={password !== "" ? "has-val input" : "input"}
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                <span className="focus-input" data-placeholder="Password"></span>
                <span
                  className="absolute right-3 top-1/2 transform -translate-y-1/2 cursor-pointer"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? <FaEyeSlash /> : <FaEye />}
                </span>
              </div>


            {error && <div className="error-message text-slate-200 ml-5 mb-5">{error}</div>}

            <div className="container-login-form-btn">
              <button type="submit" className="login-form-btn">Login</button>
            </div>

            <div className="text-center">
              <span className="txt1">Não possui conta? </span>
              <a className="txt2" href="/verify-email">
                Criar conta
              </a>
            </div>
          </form>
        </div>
      </div>
    </div>
    <footer>
      <Footer/>
    </footer>
    </>
  );
}

export default Login;