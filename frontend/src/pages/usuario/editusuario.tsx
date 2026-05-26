/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../../service/api";
import Header from "../../components/header/header";
import Footer from "../../components/footer/footer";
import { BiSend } from "react-icons/bi";
import { Perfil } from "../../types/perfil";
import { Button } from "react-bootstrap";
import { fetchIgrejas } from "../../utils/api";
import { Igreja } from "../../types/igreja";




function EditUsuario() {

    const [username, setUsername] = useState('');
    const [nome, setNome] = useState('');
    const [password, setPassword] = useState('');
    const [status, setStatus] = useState(false);
    const [igrejaId, setIgrejaId] = useState('');
    const [igreja, setIgreja] = useState<Igreja[]>([]);
    const [statusText, setStatusText] = useState('');
    const [allPerfis, setAllPerfis] = useState<Perfil[]>([]);
    const [selectedPerfis, setSelectedPerfis] = useState<string[]>([]);
    const {usuarioID} = useParams();
    const history = useNavigate();


    useEffect(() => {
        if (usuarioID === '0') return;
        else loadUsuario();
        loadPerfis();
    }, [usuarioID])

    useEffect(() => {
        setStatusText(status ? 'ATIVO' : 'INATIVO');
    }, [status]);

    useEffect(() => {
        fetchIgrejas()
        .then(response => setIgreja(response.data))
        .catch(error => console.log(error));
      }, []);
      

    async function loadUsuario() {
        try {
            const response = await api.get(`/api/usuarios/${usuarioID}`)

            setUsername(response.data.username);
            setNome(response.data.nome);
            setPassword('');
            setStatus(response.data.status);
            setIgrejaId(response.data.igrejaId);
            setSelectedPerfis(response.data.perfis || []);
        } catch (error) {
            alert('Error recovering usuario" Try again!');
            history('/listausuarios')
        }
    }

    async function loadPerfis() {
        try {
            const response = await api.get('/api/perfil');
            setAllPerfis(response.data);
        } catch (error) {
            alert('Erro ao carregar perfis!');
        }
    }

    const handlePerfilChange = (perfilNome: string) => {
        setSelectedPerfis([perfilNome]);
    };
    
    


    async function saveOrUpdate(e:{ preventDefault: () => void; }) {
        e.preventDefault();

        const data = {
            username,
            nome,
            ...(password && { password }), 
            status,
            igrejaId,
            perfis: selectedPerfis,
        }
        
        try {
            await api.put(`/api/usuarios/${usuarioID}`, data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });     
            history('/listausuarios')
        } catch (error) {
            alert('Error while recording usuario Try again!')
        }       
    }

    return (
        <>
        <Header/>
        <div className="w-screen h-screen bg-gradient-to-t from-slate-700 flex flex-col justify-center items-center">
            <h1 className="text-5xl mb-24">{usuarioID === '0' ? "'Add' " : "'Update' "}Usuário</h1>
            <form key={usuarioID}
                onSubmit={saveOrUpdate}
                className="bg-gradient-to-t from-zinc-300 w-3/6 h-3/4 
                flex flex-col items-center justify-center gap-3">

                <label htmlFor="Usuario" className="text-2xl text-rose-800" >UserName</label>
                <input type="text"
                value={username}
                onChange={e => setUsername(e.target.value)} 
                className="w-60 text-black bg-red-400 pl-2" />

                <label htmlFor="Nome" className="text-2xl text-rose-800" >Nome</label>
                <input type="text"
                value={nome}
                onChange={e => setNome(e.target.value)} 
                className="w-60 text-black bg-red-400 pl-2" />

                <label htmlFor="Password" className="text-2xl text-rose-800" >Senha</label>
                <input type="text"
                 value={password}
                onChange={e => setPassword(e.target.value)} 
                className="w-60 text-black bg-red-400 pl-2" />

                <label htmlFor="Status" className="text-2xl text-rose-800">Status</label>
                <select
                value={statusText}
                onChange={e => {
                    const value = e.target.value;
                    setStatusText(value);
                    setStatus(value === 'ATIVO');
                }}
                className="w-60 text-black bg-red-400 pl-2"
                >
                <option value="ATIVO">ATIVO</option>
                <option value="INATIVO">INATIVO</option>
                </select>


                <label htmlFor="Igreja" className="text-2xl text-rose-800" >Congregação</label>
                <input type="text"
                value={igrejaId}
                onChange={e => setIgrejaId(e.target.value)} 
                className="w-60 text-black bg-red-400 pl-2" />

                <label className="text-2xl text-rose-800">Perfis</label>
                <div className="flex flex-col items-start w-60 text-black bg-red-400 pl-2">
                    {allPerfis.map(perfil => (
                        <label key={perfil.nome} className="flex items-center gap-2">
                            <input
                                type="checkbox"
                                checked={selectedPerfis.includes(perfil.nome)}
                                onChange={() => handlePerfilChange(perfil.nome)}
                                />
                            {perfil.nome}
                            </label>
                        ))}
                </div>

                <div className="flex flex-row text-center gap-20 mb-20">

                <button type="submit" onClick={saveOrUpdate} className="w-28 h-16 flex items-center justify-center" >
                    {usuarioID === '0' ? "'Add'" : "'Update'"}
                    <BiSend title="Adicionar" color="green" className="w-20 h-16" />
                </button>    

                <Button as="a" href="/listausuarios"  className="">VOLTAR</Button>    

                </div>


            </form>
            
        </div>

        <footer>
            <Footer/>
        </footer>   
        
        
        </>
    )

}

export default EditUsuario;