/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../../../service/api";
import { BiSend } from "react-icons/bi";
import Header from "../../../components/header/header";
import Footer from "../../../components/footer/footer";
import { Button } from "react-bootstrap";

const areas = [
    { value: "AREA_01", label: "Area 1" },
    { value: "AREA_02", label: "Area 2" },
    { value: "AREA_03", label: "Area 3" },
    { value: "AREA_04", label: "Area 4" },
    { value: "AREA_05", label: "Area 5" },
    { value: "AREA_06", label: "Area 6" },
    { value: "AREA_07", label: "Area 7" },
    { value: "AREA_08", label: "Area 8" },
    { value: "AREA_09", label: "Area 9" },
    { value: "AREA_10", label: "Area 10" },
    { value: "AREA_11", label: "Area 11" }
];

function NewIgreja() {

    const [id, setId] = useState(null);
    const [nome, setNome] = useState('');
    const [endereco, setEndereco] = useState('');
    const [cnpj, setCnpj] = useState('');
    const [cep, setCep] = useState('');
    const [bairro, setBairro] = useState('');
    const [cidade, setCidade] = useState('');
    const [area, setArea] = useState('');

    const { igrejaID } = useParams();
    const history = useNavigate();

    async function loadIgreja() {
        try {
            const response = await api.get(`/api/igrejas/${igrejaID}`)

            setId(response.data.id);
            setNome(response.data.nome);
            setEndereco(response.data.endereco);
            setBairro(response.data.bairro);
            setCidade(response.data.cidade);
            setCnpj(response.data.cnpj);
            setCep(response.data.cep);
            setArea(response.data.area);

        } catch (error) {
            alert('Error recovering igreja" Try again!');
            history('/igrejas')
        }
    }

    useEffect(() => {
        if (igrejaID === '0') return;
        else loadIgreja();
    }, [igrejaID])

    const handleCnpjChange = (e: { target: { value: any; }; }) => {
        const value = e.target.value;
        const numericValue = value.replace(/\D/g, '');
        setCnpj(numericValue);
    };

    async function saveOrUpdate(e: { preventDefault: () => void; }) {
        e.preventDefault();

        const data: { id?: number | null, nome: string, endereco: string, bairro: string, cidade: string, cnpj: string, cep: string, area: string } = {
            nome,
            endereco,
            bairro,
            cidade,
            cnpj,
            cep,
            area
        }
        try {
            if (igrejaID === '0') {
                await api.post('/api/igrejas', data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            } else {
                data.id = id;
                await api.put(`/api/igrejas/${igrejaID}`, data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            }

            history('/igrejas')
        } catch (error) {
            alert('Error while recording igreja Try again!')
        }
    }

    return (
        <>
            <Header />

            <div className="min-h-screen bg-gradient-to-t from-slate-700 flex flex-col items-center px-4 py-8">
                <h1 className="text-3xl md:text-5xl font-bold text-center mb-8">
                    {igrejaID === '0' ? 'Add' : 'Update'} Igreja
                </h1>

                <form
                    key={igrejaID}
                    onSubmit={saveOrUpdate}
                    className="bg-gradient-to-t from-zinc-300 w-full max-w-2xl rounded-2xl shadow-lg flex flex-col gap-5 p-6 md:p-8"
                >
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div className="flex flex-col gap-2">
                            <label htmlFor="Nome" className="text-xl font-semibold text-rose-800">Nome</label>
                            <input
                                id="Nome"
                                type="text"
                                value={nome}
                                onChange={e => setNome(e.target.value)}
                                className="h-10 rounded-lg border border-gray-300 px-3 text-black bg-rose-400"
                            />
                        </div>

                        <div className="flex flex-col gap-2">
                            <label htmlFor="Endereco" className="text-xl font-semibold text-rose-800">Endereco</label>
                            <input
                                id="Endereco"
                                type="text"
                                value={endereco}
                                onChange={e => setEndereco(e.target.value)}
                                className="h-10 rounded-lg border border-gray-300 px-3 text-black bg-rose-400"
                            />
                        </div>

                        <div className="flex flex-col gap-2">
                            <label htmlFor="Bairro" className="text-xl font-semibold text-rose-800">Bairro</label>
                            <input
                                id="Bairro"
                                type="text"
                                value={bairro}
                                onChange={e => setBairro(e.target.value)}
                                className="h-10 rounded-lg border border-gray-300 px-3 text-black bg-rose-400"
                            />
                        </div>

                        <div className="flex flex-col gap-2">
                            <label htmlFor="Cidade" className="text-xl font-semibold text-rose-800">Cidade</label>
                            <input
                                id="Cidade"
                                type="text"
                                value={cidade}
                                onChange={e => setCidade(e.target.value)}
                                className="h-10 rounded-lg border border-gray-300 px-3 text-black bg-rose-400"
                            />
                        </div>

                        <div className="flex flex-col gap-2">
                            <label htmlFor="Cnpj" className="text-xl font-semibold text-rose-800">CNPJ</label>
                            <input
                                id="Cnpj"
                                type="text"
                                value={cnpj}
                                onChange={handleCnpjChange}
                                className="h-10 rounded-lg border border-gray-300 px-3 text-black bg-rose-400"
                                maxLength={14}
                                minLength={14}
                            />
                        </div>

                        <div className="flex flex-col gap-2">
                            <label htmlFor="Cep" className="text-xl font-semibold text-rose-800">CEP</label>
                            <input
                                id="Cep"
                                type="text"
                                value={cep}
                                onChange={e => setCep(e.target.value)}
                                className="h-10 rounded-lg border border-gray-300 px-3 text-black bg-rose-400"
                            />
                        </div>

                        <div className="flex flex-col gap-2 md:col-span-2">
                            <label htmlFor="Area" className="text-xl font-semibold text-rose-800">Area</label>
                            <select
                                id="Area"
                                value={area}
                                onChange={e => setArea(e.target.value)}
                                className="h-10 rounded-lg border border-gray-300 px-3 text-black bg-rose-400"
                            >
                                <option value="">Selecione uma area</option>
                                {areas.map((area) => (
                                    <option key={area.value} value={area.value}>{area.label}</option>
                                ))}
                            </select>
                        </div>
                    </div>

                    <div className="flex justify-center gap-4 mt-2 flex-wrap">
                        <button
                            type="submit"
                            className="flex items-center justify-center gap-2 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md h-10"
                        >
                            {igrejaID === '0' ? 'Add' : 'Update'}

                            <BiSend
                                title="Adicionar"
                                className="w-5 h-5"
                            />
                        </button>

                        <Button variant="primary" className="btn-primary" as="a" href="/igrejas">
                            VOLTAR
                        </Button>
                    </div>
                </form>
            </div>

            <footer>
                <Footer />
            </footer>
        </>
    )
}

export default NewIgreja;
