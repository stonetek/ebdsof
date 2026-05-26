/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import { fetchIgrejas, registerUser } from "../../../utils/api";
import Header from "../../../components/header/header";
import { Button } from "react-bootstrap";
import { FaUserCircle } from "react-icons/fa";
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




function Cadastro() {

  const [username, setUsername] = useState('');
  const [nome, setNome] = useState('');
  const [password, setPassword] = useState('');
  const [perfis, setPerfis] = useState<string[]>([]);
  const [strength, setStrength] = useState('');
  const [isPasswordValid, setIsPasswordValid] = useState(false);
  const [userProfile, setUserProfile] = useState('');
  const [ area, setArea] = useState('');
  const [filteredIgrejas, setFilteredIgrejas] = useState<Igreja[]>([]);
  const [igrejas, setIgrejas] = useState<Igreja[]>([])
  const [igrejaId, setIgrejaId] = useState('');
  const [showPopup, setShowPopup] = useState(false);
  const [novoUsuarioId, setNovoUsuarioId] = useState<number | null>(null);
  const [showVinculoPopup, setShowVinculoPopup] = useState(false);
  const [entidades, setEntidades] = useState<any[]>([]);
  //const [entidadeId, setEntidadeId] = useState('');

  
  
  useEffect(() => {
    const storedUserProfile = sessionStorage.getItem('userProfile');
    if (storedUserProfile) {
      setUserProfile(storedUserProfile);
    }
    fetchIgrejas()
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
  }, [area, igrejas]);

  useEffect(() => {
    if (!area || filteredIgrejas.length === 0) {
      setIgrejaId('');
    }
  }, [area, filteredIgrejas]);


  const checkPasswordStrength = (password: string) => {
        let strength = '';
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
                setIsPasswordValid(true);
                break;
            case 4:
                strength = 'Forte';
                setIsPasswordValid(true);
                break;
            case 3:
                strength = 'Média';
                setIsPasswordValid(false);
                break;
            case 2:
                strength = 'Fraca';
                setIsPasswordValid(false);
                break;
            default:
                strength = 'Muito Fraca';
                setIsPasswordValid(false);
        }

        setStrength(strength);
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
      igrejaId
    };

    const response = await registerUser(userData);

    alert('Usuário registrado com sucesso!');

    const usuarioId = response.data.id;
    setNovoUsuarioId(usuarioId);
    if (perfis[0] === "ALUNO" || perfis[0] === "PROFESSOR") {
      const tipoEntidade = perfis[0].toLowerCase();
      const endpoint =
        tipoEntidade === "aluno"
          ? `/api/alunos/por-igreja/${igrejaId}`
          : `/api/professores/por-igreja/${igrejaId}`;

      const lista = await fetch(endpoint).then(res => res.json());
      setEntidades(lista);
      setShowVinculoPopup(true);
    }

    } catch (error: any) {
      alert(`Erro ao registrar o usuário: ${error.response ? error.response.data.message : error.message}`);
    }
  };

  
  const handlePerfilChange = (e: { target: { value: string; }; }) => {
    setPerfis([e.target.value]);
  };


  

  return (
    <>
      <header>
        <Header />
        <div className="flex justify-end mt-4 mr-5">
          {userProfile === 'ROLE_ADMIN' && (
              <Button as="a" href="/listausuarios">
                <span>Lista Usuarios</span>
                <FaUserCircle className="ml-8 bg-cyan-500 rounded-full w-8 h-8"/>
              </Button>
          )}  
        </div>
      </header>


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
            
          <div className="form-group mb-3">
            <label htmlFor="username">E-mail</label>
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
            
          <div className="form-group">
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
            <div className={`password-strength ${strength.toLowerCase().replace(' ', '-')} mb-3`}>
                Força da senha: {strength}
            </div>
          </div>

          <div className="form-group">
          <label htmlFor="perfil">Perfil</label>
          <select
                className="form-control"
                id="perfil"
                value={perfis[0] || ''} 
                onChange={handlePerfilChange}
                required
              >
                <option value="">Selecione um perfil</option>
                {userProfile === 'ROLE_ADMIN' && <option value="ADMIN">Administrador</option>}
                {userProfile === 'ROLE_ADMIN' &&<option value="ADMIN_IGREJA">Admin_Igreja</option>}
                <option value="SECRETARIA">Secretaria</option>
                {userProfile === 'ROLE_ADMIN' &&<option value="coordenador">Coordenador</option>}
                <option value="PROFESSOR">Professor</option>
                <option value="ALUNO">Aluno</option>
          </select>
          </div>


          <label htmlFor="Area" className="text-2xl text-rose-800 mt-4 mr-2" >Área</label>
            <select 
                value={area} 
                onChange={e => setArea(e.target.value)} 
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
                disabled={!area || filteredIgrejas.length === 0}
              >
                <option value="">
                  {!area ? "Selecione uma área primeiro" : filteredIgrejas.length === 0 ? "Nenhuma igreja disponível" : "Selecione uma igreja"}
                </option>
                {filteredIgrejas.map((igreja) => (
                  <option key={igreja.id} value={igreja.id}>{igreja.nome}</option>
                ))}
              </select>
              <p className="mt-3 font-bold text-rose-800">AVISO! Para o perfil ADMINISTRADOR menhuma igreja deve ser selecionada!</p>
            </div>
            <button type="submit" disabled={!isPasswordValid} className="btn btn-primary mt-2">Registrar</button>
            <Button variant="primary" className='ml-10 mt-2' as="a" href="/home">VOLTAR</Button>
        </form>
      </div>

      <footer>
        <Footer />
      </footer>
    </>
  );
  
}

export default Cadastro;