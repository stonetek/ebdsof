/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react";
import { BiSend } from "react-icons/bi";
import { useParams, useNavigate} from "react-router-dom";
import api from '../../../service/api'
import Header from "../../../components/header/header";
import Footer from "../../../components/footer/footer";
import { Button } from "react-bootstrap";



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

function NewAluno() {

    const [ id, setId] = useState(null);
    const [ nome, setNome] = useState('');
    const [ aniversario, setAniversario] = useState('');
    const [ area, setArea] = useState('');
    const [ novoConvertido, setNovoConvertido] = useState('');
    const [ sexo, setSexo] = useState('');
    const {alunoID} = useParams();

    const igrejaId = sessionStorage.getItem('igrejaId');
    //const areaAtual = sessionStorage.getItem('areaAtual');

    const history = useNavigate();

    async function loadAluno() {
        try {
            const response = await api.get(`/api/alunos/${alunoID}`)
            
            const adjustedDate = response.data.aniversario.split("T", 10)[0];
            setId(response.data.id);
            setNome(response.data.nome);
            setAniversario(adjustedDate);
            setArea(response.data.area);
            setNovoConvertido(response.data.novoConvertido);
            setSexo(response.data.sexo);
        } catch (error) {
            alert('Error recovering aluno" Try again!');
            history('/alunos')
        }
    }

    useEffect(() => {
    if (alunoID === '0') {
        const igrejaId = sessionStorage.getItem('igrejaId');
        const areaAtual = sessionStorage.getItem('areaAtual');

        // 🔹 Só define automaticamente se igrejaId e areaAtual existirem
        if (igrejaId && areaAtual) {
        setArea(areaAtual);
        }
    } else {
        loadAluno();
    }
    }, [alunoID]);


    async function saveOrUpdate(e:{ preventDefault: () => void; }) {
        e.preventDefault();

        const igrejaId = sessionStorage.getItem('igrejaId');
        
        const data = {
            nome,
            aniversario,
            area,
            novoConvertido: novoConvertido === 'SIM',
            sexo,
            igrejaId: igrejaId ? parseInt(igrejaId, 10) : null,
            id: alunoID !== '0' ? id : undefined,
        }
        try {
            if (alunoID === '0') {
                await api.post('/api/alunos', data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            } else {
                data.id = id;
                await api.put(`/api/alunos/${alunoID}`, data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            }

            history('/alunos')
        } catch (error) {
            alert('Error while recording aluno Try again!')
        }       
    }


    return (
        <>
          <Header/>
            <div className="w-full min-h-screen bg-gradient-to-t from-slate-700 flex flex-col justify-center items-center mt-10 p-4">
                <h1 className="text-3xl sm:text-4xl md:text-5xl mb-10 text-center">{alunoID === '0' ? "'Add' " : "'Update' "}Aluno</h1>

                <form 
                    key={alunoID}
                    onSubmit={saveOrUpdate}
                    className="bg-gradient-to-t from-zinc-300 w-full sm:w-5/6 md:w-3/4 lg:w-3/6 max-w-2xl
                    flex flex-col items-center justify-center gap-3 rounded-5 p-4 md:p-6">
                    <label htmlFor="Nome" className="text-2xl text-rose-800" >Nome</label>
                    <input type="text"
                    value={nome}
                    onChange={e => setNome(e.target.value)} 
                    className="w-60 text-black bg-red-400 p-2" />

                    <label htmlFor="Niver" className="text-2xl text-rose-800" >Aniversário</label>
                    <input type="date"
                    value={aniversario}
                    onChange={e => setAniversario(e.target.value)} 
                    className="w-60 text-black bg-red-400 p-2" />

                    <label htmlFor="Area" className="text-2xl text-rose-800" >Área</label>
                    <select
                        value={area}
                        onChange={e => setArea(e.target.value)}
                        className="w-60 text-black bg-red-400 p-2"
                        disabled={!!igrejaId && !!sessionStorage.getItem('areaAtual')}
                        >
                        <option value="">Selecione uma área</option>
                        {areas.map((area) => (
                            <option key={area.value} value={area.value}>{area.label}</option>
                        ))}
                    </select>

                    <label htmlFor="NovoConvertido" className="text-2xl text-rose-800">Novo Convertido</label>
                    <select
                            value={novoConvertido}
                            onChange={e => setNovoConvertido(e.target.value)}
                            className="w-60 text-black bg-red-400 p-2"
                        >
                            <option value="">Selecione</option>
                            <option value="SIM">SIM</option>
                            <option value="NÃO">NÃO</option>
                    </select>
                    
                    <label htmlFor="Sexo" className="text-2xl text-rose-800">Sexo</label>
      
                    <select
                        value={sexo}
                        onChange={e => setSexo(e.target.value)}
                        className="w-60 text-black bg-red-400 p-2"
                    >
                        <option value="">Selecione</option>
                        <option value="M">M</option>
                        <option value="F">F</option>
                    </select>

                    <div className="flex text-center gap-5">

                    <button type="submit" onClick={saveOrUpdate} className="w-24 h-10 flex items-center justify-center bg-green-500 hover:bg-green-600 text-white rounded-lg" >
                        {alunoID === '0' ? "'Add'" : "'Update'"}
                        <BiSend title="Adicionar" color="white" className="w-8 h-8" />
                    </button>

                    <Button variant="primary" className='btn-primary' as="a" href="/alunos">VOLTAR</Button>

                    </div>
                                                    
                </form>

                <footer className="w-screen mt-5">
                    <Footer/>
                </footer>
            </div>          
        </>
    )
}

export default NewAluno;

