import { ChangeEvent, useEffect, useMemo, useState } from "react";
import { Button, Container, FormControl, Pagination, Table } from "react-bootstrap";
import {
    fetchNiver,
    fetchNiverPorIgreja,
    fetchNiverTrimestre,
    fetchNiverTrimestrePOrIgreja
} from "../../utils/api";
import { formatLocalDate } from "../../utils/format";

type Aniversariante = {
    id: number;
    nome: string;
    aniversario: string;
    professorAulas?: { id: number }[];
    professorTurmas?: { id: number }[];
};

function getTipo(aniversariante: Aniversariante) {
    return aniversariante.professorAulas || aniversariante.professorTurmas
        ? "Professor"
        : "Aluno";
}

function getAniversarianteKey(aniversariante: Aniversariante) {
    return `${getTipo(aniversariante)}-${aniversariante.id}`;
}

function removerDuplicados(lista: Aniversariante[]) {
    const aniversariantesUnicos = new Map<string, Aniversariante>();

    lista.forEach((aniversariante) => {
        aniversariantesUnicos.set(getAniversarianteKey(aniversariante), aniversariante);
    });

    return Array.from(aniversariantesUnicos.values());
}

function Aniversarios() {
    const [aniversariantesMes, setAniversariantesMes] = useState<Aniversariante[]>([]);
    const [aniversariantesTrimestre, setAniversariantesTrimestre] = useState<Aniversariante[]>([]);
    const [selecao, setSelecao] = useState("mes");
    const [searchTerm, setSearchTerm] = useState("");
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(5);

    useEffect(() => {
        const igrejaId = sessionStorage.getItem("igrejaId");
        const request = igrejaId && !isNaN(Number(igrejaId))
            ? fetchNiverPorIgreja(Number(igrejaId))
            : fetchNiver();

        request
            .then(response => setAniversariantesMes(removerDuplicados(response.data)))
            .catch(error => console.log(error));
    }, []);

    useEffect(() => {
        const igrejaId = sessionStorage.getItem("igrejaId");
        const request = igrejaId && !isNaN(Number(igrejaId))
            ? fetchNiverTrimestrePOrIgreja(Number(igrejaId))
            : fetchNiverTrimestre();

        request
            .then(response => setAniversariantesTrimestre(removerDuplicados(response.data)))
            .catch(error => console.log(error));
    }, []);

    const handleSearchChange = (event: ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(event.target.value);
        setCurrentPage(1);
    };

    const handleSelecaoChange = (event: ChangeEvent<HTMLSelectElement>) => {
        setSelecao(event.target.value);
        setCurrentPage(1);
    };

    const filteredAniversariantes = useMemo(() => {
        const aniversariantes = selecao === "mes"
            ? aniversariantesMes
            : aniversariantesTrimestre;

        return aniversariantes.filter(aniversariante =>
            aniversariante.nome.toLowerCase().includes(searchTerm.toLowerCase())
        );
    }, [aniversariantesMes, aniversariantesTrimestre, searchTerm, selecao]);

    const totalPages = Math.ceil(filteredAniversariantes.length / itemsPerPage);
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentAniversariantes = filteredAniversariantes.slice(indexOfFirstItem, indexOfLastItem);

    const handlePageChange = (page: number) => {
        if (page >= 1 && page <= totalPages) {
            setCurrentPage(page);
        }
    };

    return (
        <Container>
            <div>
                <h1>Aniversariantes</h1>
                <div>Escolha mês ou trimestre</div>

                <select value={selecao} onChange={handleSelecaoChange} className="mb-3">
                    <option value="mes">Mês</option>
                    <option value="trimestre">Trimestre</option>
                </select>

                <FormControl
                    type="text"
                    placeholder="Pesquisar por nome"
                    value={searchTerm}
                    onChange={handleSearchChange}
                    className="mb-3"
                />

                <Table striped bordered hover variant="dark">
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Aniversário</th>
                            <th>Tipo</th>
                        </tr>
                    </thead>
                    <tbody>
                        {currentAniversariantes.map(aniversariante => (
                            <tr key={getAniversarianteKey(aniversariante)}>
                                <td>{aniversariante.nome}</td>
                                <td>{formatLocalDate(aniversariante.aniversario, "dd/MM/yyyy")}</td>
                                <td>{getTipo(aniversariante)}</td>
                            </tr>
                        ))}
                    </tbody>
                </Table>

                <Pagination className="justify-content-center">
                    <Pagination.Prev
                        onClick={() => handlePageChange(currentPage - 1)}
                        disabled={currentPage === 1}
                    />

                    {[...Array(totalPages)].map((_, index) => (
                        <Pagination.Item
                            key={index}
                            active={index + 1 === currentPage}
                            onClick={() => handlePageChange(index + 1)}
                        >
                            {index + 1}
                        </Pagination.Item>
                    ))}

                    <Pagination.Next
                        onClick={() => handlePageChange(currentPage + 1)}
                        disabled={currentPage === totalPages || totalPages === 0}
                    />
                </Pagination>

                <Button variant="primary" className="btn-primary mb-3 mt-3" as="a" href="/home">
                    VOLTAR
                </Button>
            </div>
        </Container>
    )
}

export default Aniversarios;
