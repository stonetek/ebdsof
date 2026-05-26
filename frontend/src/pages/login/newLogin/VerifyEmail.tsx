import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../../service/api'; // importa seu axios configurado
import axios from 'axios';
import Footer from '../../../components/footer/footer';
import Header from '../../../components/header/header';

function VerifyEmail() {
  const [email, setEmail] = useState('');
  const [code, setCode] = useState('');
  const [step, setStep] = useState(1);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const sendCode = async () => {
  try {
    await api.post('/api/auth/send-code', { email });
    setStep(2);
    setError('');
  } catch (err: unknown) {
    if (axios.isAxiosError(err)) {
      const errorMessage =
        typeof err.response?.data === 'string'
          ? err.response.data
          : err.response?.data?.message || 'Erro ao enviar código.';

      setError(errorMessage);
    } else {
      setError('Erro desconhecido.');
    }
  }
};


  const verifyCode = async () => {
      try {
        const response = await api.post('/api/auth/validate-code', { email, code });
        const token = response.data.token;
        localStorage.setItem('register_token', token); // ou usar context
    
        navigate('/register');
      } catch (err: unknown) {
       if (axios.isAxiosError(err)) {
  const errorMessage =
    typeof err.response?.data === 'string'
      ? err.response.data
      : err.response?.data?.message || 'Erro ao enviar código.';
  setError(errorMessage);
} else {
  setError('Erro desconhecido.');
}
      }
  };
    

  return (
    <div>

      <header>
        <Header/>
      </header>
      
      <div className="mt-5 w-screen h-screen p-10">
        <h2>Verificação de E-mail</h2>

        {step === 1 && (
          <>
            <input
              type="email"
              placeholder="Digite seu e-mail"
              className="form-control mb-10"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <button className="btn btn-primary" onClick={sendCode}>
              Enviar Código
            </button>
          </>
        )}

        {step === 2 && (
          <>
            <input
              type="text"
              placeholder="Digite o código recebido"
              className="form-control mb-3"
              value={code}
              onChange={(e) => setCode(e.target.value)}
            />
            <button className="btn btn-success" onClick={verifyCode}>
              Validar Código
            </button>
          </>
        )}

        {error && (
          <div className="alert alert-danger mt-3">
            <strong>Erro:</strong> {error}
          </div>
        )}
      </div>
      
      <footer>
        <Footer/>
      </footer>
    </div>  
  );
}

export default VerifyEmail;
