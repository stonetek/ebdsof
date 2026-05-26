/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import { fetchIgrejasPublicas, registerUser } from "../../../utils/api";
import Header from "../../../components/header/header";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Footer from "../../../components/footer/footer";
import { Igreja } from "../../../types/igreja";



const areas = [
  { value: "AREA_01", label: "Área 1" },
  { value: "AREA_02", label: "Área 2" },
  { value: "AREA_03", label: "Área 3" },
  { value: "AREA_04", label: "Área 4" },
  { value: "AREA_05", label: "Área 5" },
  { value: "AREA_06", label: "Área 6" },
  { value: "AREA_07", label: "Área 7" },
  { value: "AREA_08", label: "Área 8" },
  { value: "AREA_09", label: "Área 9" },
  { value: "AREA_10", label: "Área 10" },
  { value: "AREA_11", label: "Área 11" }
];



function Register() {

  const [username, setUsername] = useState('');
  const [nome, setNome] = useState('');
  const [password, setPassword] = useState('');
  const [strength, setStrength] = useState('');
  const [isPasswordValid, setIsPasswordValid] = useState(false);
  const [color, setColor] = useState('');
  const [perfis, setPerfis] = useState<string[]>([]);
  const [igrejas, setIgrejas] = useState<Igreja[]>([])
  const [igrejaId, setIgrejaId] = useState('');
  const [ area, setArea] = useState('');
  const [filteredIgrejas, setFilteredIgrejas] = useState<Igreja[]>([]);
  const [showPopup, setShowPopup] = useState(false);
  //const [isButtonDisabled, setIsButtonDisabled] = useState(true);
  

 


  const navigate = useNavigate();

  useEffect(() => {
    fetchIgrejasPublicas()
      .then(response => setIgrejas(response.data))
      .catch(error => console.log(error));
  }, []);

  
  useEffect(() => {
    if (area) {
      const filtered = igrejas.filter(igreja => igreja.area === area);
      setFilteredIgrejas(filtered);
      if (filtered.length === 0) {
        setShowPopup(true);
      } else {
        setShowPopup(false);
      }
    } else {
      setFilteredIgrejas([]);
      setShowPopup(false);
    }
    //checkButtonState();
  }, [area, igrejas]);

  
  

   const handleAreaChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newArea = e.target.value;
    setArea(newArea);
    if (newArea) {
      fetchIgrejasPublicas()
        .then(response => {
          const all = Array.isArray(response.data) ? response.data : [];
          setIgrejas(all);
          const filtered = all.filter(igreja => igreja.area === newArea);
          setFilteredIgrejas(filtered);
          setShowPopup(filtered.length === 0);
          //checkButtonState();
        })
        .catch(error => {
          console.log(error);
          setFilteredIgrejas([]);
          setShowPopup(false);
        });
    } else {
      setFilteredIgrejas([]);
      setShowPopup(false);
      //checkButtonState();
    }
  };
  
  
  const checkPasswordStrength = (password: string) => {
    let strength = '';
    let color = '';
    const regexes = {
        uppercase: /[A-Z]/,
        lowercase: /[a-z]/,
        number: /[0-9]/,
        specialChar: /[#?!@$%^&*-]/,
        minLength: /.{8,}/,
    };

    const passedCriteria = Object.values(regexes).filter((regex) => regex.test(password)).length;

    switch (passedCriteria) {
        case 5:
            strength = 'Muito Forte';
            color = 'green';
            setIsPasswordValid(true);
            break;
        case 4:
            strength = 'Forte';
            color = 'blue';
            setIsPasswordValid(true);
            break;
        case 3:
            strength = 'Média';
            color = 'orange';
            setIsPasswordValid(false);
            break;
        case 2:
            strength = 'Fraca';
            color = 'red';
            setIsPasswordValid(false);
            break;
        default:
            strength = 'Muito Fraca';
            color = 'darkred';
            setIsPasswordValid(false);
    }

    setStrength(strength);
    setColor(color);
  };

  const handlePerfilChange = (e: { target: { value: string; }; }) => {
    setPerfis([e.target.value]);
  };

  const isValidEmail = (email: string) => {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }


  const handleSubmit = async (event: { preventDefault: () => void; }) => {
    event.preventDefault();
    if (!isValidEmail(username)) {
      alert('Por favor, informe um e-mail válido no campo Usuário.');
      return;
    }
    try {
      const userData: any = {
        username,
        nome,
        password,
        perfis,
      };
      if (igrejaId) {
        userData.igrejaId = igrejaId;
      }
      const response = await registerUser(userData);
      alert('Usuário registrado com sucesso!');
      navigate('/');
    } catch (error: any) {
      alert(`Erro ao registrar o usuário: ${error.response ? error.response.data.message : error.message}`);
    }
  };

  return (
    <>
      <Header showOnlyWelcome/>
        <div className="container mt-5 h-screen">
          <h2>Registrar Novo Usuário</h2>
          <form onSubmit={handleSubmit}>

            <div className="form-group mb-3">
              <label htmlFor="nome">Nome</label>
              <input
                  type="text"
                  className="form-control"
                  id="nome"
                  value={nome}
                  onChange={(e) => setNome(e.target.value)}
                  required
              />
            </div>

            <div className="form-group">
                <label htmlFor="username">Usuário</label>
                <input
                    type="email"
                    className="form-control"
                    id="username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                    onInvalid={(e) => (e.target as HTMLInputElement).setCustomValidity('Por favor, informe um e-mail válido.')}
                    onInput={(e) => (e.target as HTMLInputElement).setCustomValidity('')}
                />
            </div>

            <div className="form-group mt-4">
              <label htmlFor="perfil">Perfil</label>
              <select
                className="form-control"
                id="perfil"
                value={perfis[0] || ''} 
                onChange={handlePerfilChange}
                required
              >
                <option value="">Selecione um perfil</option>
                <option value="PROFESSOR">PROFESSOR</option>
                <option value="ALUNO">ALUNO</option>
              </select>
            </div>

            <label htmlFor="Area" className="text-2xl text-rose-800 mt-4 mr-2" >Área</label>
            <select 
              value={area} 
              onChange={handleAreaChange} 
              className="w-60 text-black bg-red-400 pl-2"
            >

            <option value="">Selecione uma área</option>
              {areas.map((area) => (
              <option key={area.value} value={area.value}>{area.label}</option>
              ))}
            </select>

            {showPopup && (
                <div className="popup">
                  <p>Não há igrejas na área selecionada. Por favor, entre em contato com o administrador do sistema.</p>
                  <button onClick={() => setShowPopup(false)}>Fechar</button>
                </div>
            )}

            <div className="form-group mt-4">
                <label htmlFor="igreja">Igreja</label>
                <select
                  className="form-control"
                  id="igreja"
                  value={igrejaId}
                  onChange={(e) => setIgrejaId(e.target.value)}
                >
                  <option value="">Selecione sua congregação</option>
                  {filteredIgrejas.map(igreja => (
                    <option key={igreja.id} value={igreja.id}>{igreja.nome}</option>
                  ))}
                </select>
            </div>

            <div className="form-group mt-4">
                  <label htmlFor="password">Senha</label>
                  <input
                      type="password"
                      className="form-control"
                      id="password"
                      value={password}
                      onChange={(e) => {
                          const newPassword = e.target.value;
                          setPassword(newPassword);
                          checkPasswordStrength(newPassword);
                      }}
                      required
                  />
                  <div className={`password-strength ${strength.toLowerCase().replace(' ', '-')} mb-3 `}>
                      Força da senha: 
                      <span className="ml-2 font-bold " style={{ color }}>{strength}</span>
                  </div>
            </div>


           <button
              type="submit"
              className="btn btn-primary mt-2"
              disabled={!(username && password && igrejaId && isPasswordValid)}
            >
              Registrar
            </button>

            <Button variant="primary" className='ml-10 mt-2' as="a" href="/">
              VOLTAR
            </Button>

          </form>
        </div>
        <footer className="mt-20">
          <Footer/>
        </footer>
    </>
  );
  
}

export default Register;