/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../../../service/api";
import Header from "../../../components/header/header";
import Footer from "../../../components/footer/footer";
import { Button } from "react-bootstrap";
import { BiSend } from "react-icons/bi";

function NewRevista() {

    const [ id, setId] = useState(null);
    const [ nome, setNome] = useState('');
    const [ formato, setFormato] = useState('');
    const [ tipo, setTipo] = useState('');
    const [ preco, setPreco] = useState('');
    const {revistaID} = useParams();

    const history = useNavigate();

    async function loadRevista() {
        try {
            const response = await api.get(`/api/revistas/${revistaID}`)
            
            setId(response.data.id);
            setNome(response.data.nome);
            setFormato(response.data.formato);
            setTipo(response.data.tipo);
            setPreco(response.data.preco);
        } catch (error) {
            alert('Error recovering aluno" Try again!');
            history('/revistas')
        }
    }

    useEffect(() => {
        if (revistaID === '0') return;
        else loadRevista();
    }, [revistaID])

    async function saveOrUpdate(e:{ preventDefault: () => void; }) {
        e.preventDefault();

        const data = {
            nome,
            formato,
            tipo,
            preco,
            id: revistaID !== '0' ? id : undefined,
        };
        try {
            if (revistaID === '0') {
                await api.post('/api/revistas/novarevista', data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            } else {
                data.id = id;
                await api.put(`/api/revistas/atualizar/${revistaID}`, data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            }

            history('/revistas')
        } catch (error) {
            alert('Error while recording revista Try again!')
        }       
    }


    return (
        <>
            <Header/>
            <h1 className="text-5xl text-center gap-4">{revistaID === '0' ? "'Add' " : "'Update' "}Revista</h1>
            <div className="">    
                <div className="w-screen h-screen bg-gradient-to-t from-slate-700 flex flex-col justify-center items-center">

                    <form 
                        key={revistaID}
                        onSubmit={saveOrUpdate}
                        className="bg-gradient-to-t from-zinc-300 w-3/4 h-3/4 
                        flex flex-col items-center justify-center gap-3 rounded-5">
                        <label htmlFor="Nome" className="text-2xl text-rose-800">Nome</label>
                        <input type="text"
                        value={nome}
                        onChange={e => setNome(e.target.value)} 
                        className="w-60 text-black bg-red-400 p-1 rounded-2" />
                        
                        <label htmlFor="Formato" className="text-2xl text-rose-800">Formato</label>
                        <select
                            value={formato}
                            onChange={e => setFormato(e.target.value)}
                            className="w-60 text-black bg-red-400 p-1 rounded-2"
                        >
                            <option value="">Selecione o Formato</option>
                            <option value="Normal">Normal</option>
                            <option value="Capa dura">Capa dura</option>
                            <option value="Ampliada">Ampliada</option>
                            <option value="Livro">Livro</option>
                        </select>

                        <label htmlFor="Tipo" className="text-2xl text-rose-800">Tipo</label>
                        <select
                            value={tipo}
                            onChange={e => setTipo(e.target.value)}
                            className="w-60 text-black bg-red-400 pl-2 p-1 rounded-2"
                        >
                            <option value="">Selecione o Tipo</option>
                            <option value="ALUNO">ALUNO</option>
                            <option value="PROFESSOR">PROFESSOR</option>
                        </select>

                        <label htmlFor="Valor" className="text-2xl text-rose-800">Valor</label>
                        <input type="number"
                            step="0.01"
                            value={preco}
                            onChange={e => setPreco(e.target.value)}
                        className="w-60 text-black bg-red-400 pl-2 p-1 rounded-2"/>

                        <div className="flex flex-row sm:flex-row justify-center items-center gap-4 mt-10 mb-5">

                            <button type="submit" onClick={saveOrUpdate} className="flex items-center justify-center gap-2 bg-green-500 hover:bg-green-700
                                text-white font-bold py-2 px-5 rounded transition">
                                {revistaID === '0' ? "'Add'" : "'Update'"}
                                <BiSend title="Adicionar" color="white" size={24} />
                            </button>

                            <Button variant="primary" className='btn-primary' as="a" href="/revistas">VOLTAR</Button>

                        </div>
                                                        
                    </form>

                    <footer className="w-screen mt-5">
                        <Footer/>
                    </footer>
                </div>
            </div>          
        </>
    )
}

export default NewRevista