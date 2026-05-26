import { useEffect, useState } from "react";
import { Usuario } from "../../types/usuario";
import { fetchUsuario } from "../../utils/api";
import { Button, Pagination, Table } from "react-bootstrap";
import { Link } from "react-router-dom";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import Header from "../../components/header/header";
import Footer from "../../components/footer/footer";
import Menu from "../../components/menu/Menu";

function Usuarios() {

    const [usuario, setUsuario] = useState<Usuario[]>([]);
    const [search, setSearch] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(5);
    const [userProfile, setUserProfile] = useState<string>('');

    useEffect(() => {
        fetchUsuario().then(response => setUsuario( response.data))
        .catch(error => console.log(error))
    }, []);

    useEffect(() => {
        const profile = sessionStorage.getItem('userProfile');
        if (profile !== null) {
          setUserProfile(profile);
        }
    }, []);
    
    
    const handlePageChange = (page: number) => {
        setCurrentPage(page);
    };


    const lowerSearch = search.toLowerCase();
    const filteredUsuarios = usuario.filter((usuario) => usuario.
    username.toLowerCase().includes(lowerSearch));

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentUsuarios = filteredUsuarios.slice(
        indexOfFirstItem,
        indexOfLastItem
    );


    return (
        <>
           <div className="flex flex-col mb-4">
                <Header />
                <Menu />
                <h3 className="flex text-slate-800 w-screen justify-center mt-4 -mb-2">Lista Usuários</h3>
            </div>

            
            <div className="container h-screen">
                <div className="flex justify-between mb-5 rounded-3xl">
                    <div className="flex justify-start w-screen">
                    <input
                        className="mr-2 text-center bg-slate-500"
                        type="text"
                        value={search}
                        onChange={event => setSearch(event.target.value)}
                        placeholder="digite nome"
                    />
                    </div>
                    {userProfile === 'administrador' && (
                        <a href="/registers" className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md mt-12" title="NOVO">
                        <BsFillPlusCircleFill className="w-8 h-8 text-white"/>
                        </a>
                    )}
                </div>

                <Table striped bordered hover variant="dark">
                    <thead>
                    <tr>
                        <th></th>
                        <th>Nome</th>
                        <th>UserName</th>
                        <th>Perfil</th>
                        <td>Ação</td>
                    </tr>
                    </thead>
                    <tbody>
                    {currentUsuarios.map((usuario, index) => (
                        <tr key={usuario.id}>
                        <td>{index + 1}</td>
                        <td>{usuario.nome}</td>
                        <td>{usuario.username}</td>
                        <td>{usuario.perfis}</td>
                        <td>{
                            
                            <div>

                            <Link to={`/usuariosedicao/${usuario.id}`}>
                                <button className="w-14 h-10 flex-col" title="EDITAR">
                                    <BsPencil className="w-20 h-6 mt-2" color="yellow"/>
                                </button> 
                            </Link>

                            </div>
                        
                        }</td>
                        </tr>
                    ))}
                    </tbody>
                </Table>

                {/* Paginação */}
                <Pagination className="justify-content-center">
                    <Pagination.Prev
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 1}
                    />
                    {[...Array(Math.ceil(filteredUsuarios.length / itemsPerPage))].map(
                    (_, index) => (
                        <Pagination.Item
                        key={index}
                        active={index + 1 === currentPage}
                        onClick={() => handlePageChange(index + 1)}
                        >
                        {index + 1}
                        </Pagination.Item>
                    )
                    )}
                    <Pagination.Next
                    onClick={() => handlePageChange(currentPage + 1)}
                    disabled={currentPage === Math.ceil(filteredUsuarios.length / itemsPerPage)}
                    />
                </Pagination>

                <Button as="a" href="/registers" >VOLTAR</Button>
            
            </div>

            <footer>
                <Footer />
            </footer>
        </>
    );


}

export default Usuarios;