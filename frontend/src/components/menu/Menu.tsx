import { useState } from 'react';
import { Link, useLocation  } from 'react-router-dom';
import './style.css';

function Menu() {
    const [isEbdMenuOpen, setIsEbdMenuOpen] = useState(false);
    const location = useLocation();

    const toggleEbdMenu = () => {
        setIsEbdMenuOpen(!isEbdMenuOpen);
    };

    const getMenuLinks = () => {
        if (location.pathname.startsWith('/escolabiblica')) {
            return (
                <>
                <Link to="/escolabiblicaEclasses" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                    Classes E EBD
                </Link>

                <Link to="/aulas" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                    Lições
                </Link>

                <Link to="/professores" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                    Professores
                </Link>

                <Link to="/classes" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                    Classes
                </Link>

                </>
            );
        } else if (location.pathname.startsWith('/alunos')) {
            return (
                <>
                
                <Link to="/alunosEturmas" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                    Aluno Turma
                </Link>

                <Link to="/alunosEaulas" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                    Aluno Aula
                </Link>
                
                </>
            );
        } else if (location.pathname.startsWith('/igrejas')) {
            return (
                <Link to="/igrejaEebd" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                    Igreja EBD Vínculo
                </Link>
            );
        } else if (location.pathname.startsWith('/professores')) {
            return (
                <>
                    <Link to="/professorEturmas" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                        Professor e Turma
                    </Link>
                    <Link to="/professorEaulas" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                        Professor e Aula
                    </Link>
                </>
            );
        }else if (location.pathname.startsWith('/aulas')) {
            return (
                <Link to="/aulasEturmas" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                    Aula Turma
                </Link>
            );
        } else if (location.pathname.startsWith('/listausuarios')) {
            return (

                <>
                
                <Link to="/vincularusuario" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                    Vinculo de Usuário
                </Link>

                <Link to="/vincularperfil" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                    Vinculo de Perfil
                </Link>

                </>
            );
        }else if (location.pathname.startsWith('/pedidos')) {
            return (

                <>
                
                <Link to="/pagamentos" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                    Pedidos a Pagar
                </Link>

                </>
            );
        }else if (location.pathname.startsWith('/pagamentos')) {
            return (

                <>

                    <Link to="/parcelas" className="submenu-item block px-2 py-2 text-white hover:bg-gray-600">
                        Parcelas
                    </Link>

                </>
            );
        }
        // Add more conditions here if there are more URLs
        return null;
    };

    return (
       <div className="menu-container bg-gray-800 text-white flex flex-wrap justify-between p-4">
            <div className="menu-item px-4 relative">
                <button onClick={toggleEbdMenu} className="focus:outline-none">
                EBD MENU
                </button>
                {isEbdMenuOpen && (
                <div className="submenu absolute top-full left-0 bg-slate-400 mt-2 p-2 rounded shadow-lg w-48 sm:w-56 md:w-64">
                    {getMenuLinks()}
                </div>
                )}
            </div>
        </div>

    );
}

export default Menu;
