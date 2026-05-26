import { Navigate, Route, Routes } from "react-router-dom";
import Home from "../pages/home/home";
import Igreja from "../pages/igreja/igreja";
import Aluno from "../pages/aluno/aluno";
import Aula from "../pages/aula/aula";
import Ebd from "../pages/ebd/ebd";
import Professor from "../pages/professor/professor";
import Turma from "../pages/classe/turma";
import NewAluno from "../pages/aluno/new/newAluno";
import NewAula from "../pages/aula/new/newAula";
import NewProfessor from "../pages/professor/newProfessor/newProfessor";
import NewIgreja from "../pages/igreja/newIgreja/NewIgreja";
import NewTurma from "../pages/classe/newclasse/newTurma";
import Vinculos from "../pages/vinculos/Vinculos";
import ProfessorTurma from "../components/tables/tableProfessorTurma";
import DataTableTrimestre from "../components/tables/tableTrimestre";
import NewEbd from "../pages/ebd/newEbd/newEbd";
import Ofertas from "../pages/ofertas/ofertas";
import Matricula from "../pages/matricula/matricula";
import EbdAndTurmas from "../pages/ebd/edbTurmas/EbdTurmas";
import NewEbdTurmas from "../pages/ebd/newEbd/newEbdTurmas";
import AlunoAndTurmas from "../pages/aluno/alunoTurma/alunoAndTurmas";
import IgrejaAndEbd from "../pages/igreja/igrejaEbd/igrejaAndEbd";
import NewIgrejaEbd from "../pages/igreja/igrejaEbd/newIgrejaEbd";
import ProfessorAndTurmas from "../pages/professor/professorTurma/professorTurma";
import NewProfessorTurmas from "../pages/professor/professorTurma/newProfessorTurmas";
import ProfessorAndAulas from "../pages/professor/professorAula/professorAula";
import NewProfessorAula from "../pages/professor/professorAula/newProfessorAula";
import AulaAndTurmas from "../pages/aula/aulaTurma/aulaTurma";
import NewAulaTurma from "../pages/aula/aulaTurma/newAulaTurma";
import NewAlunoTurma from "../pages/aluno/alunoTurma/newAlunoTurma";
import AlunoAndAulas from "../pages/aluno/alunoAula/alunoAula";
import NewAlunoAula from "../pages/aluno/alunoAula/newAlunoAula";
import IgrejaEbdTurmaFind from "../components/tables/ebdTurmas/tableIgrejaEbdTurma";
import Login from "../pages/login/Login";
import PrivateRoute from "./privateRoutes";
import Register from "../pages/login/newLogin/cadastroUsuario";
import Cadastro from "../pages/login/newLogin/newCadastro";
import AulasPorAluno from "../pages/aluno/alunoAula/aulasPorAluno";
import Matriculas from "../pages/matricula/matrix";
import Usuarios from "../pages/usuario/usuarios";
import EditAlunoTurma from "../pages/aluno/alunoTurma/editAlunoturma";
import Report from "../pages/report/report";
import EditUsuario from "../pages/usuario/editusuario";
import VincularUsuario from "../pages/usuario/vinculoUsuario";
import VincularPerfil from "../pages/perfil/vincularPerfil";
import Revistas from "../pages/revista/revistas";
import NewRevista from "../pages/revista/newRevista/newRevista";
import Pedidos from "../pages/pedido/pedido";
import NewPedidos from "../pages/pedido/new/newPedido";
import Pagamentos from "../pages/pagamento/pagamento";
import Parcelas from "../pages/parcela/parcela.tsx";
import VerifyEmail from "../pages/login/newLogin/VerifyEmail.tsx";



const PrivateRegisterRoute = ({ children }: { children: React.ReactNode }) => {
    const token = localStorage.getItem('register_token');
    if (!token) {
      return <Navigate to="/verify-email" replace />;
    }
    return <>{children}</>;
  };

function MainRoutes () {
    return (
        <Routes>
            <Route path='/' element={<Login/>}/>
            <Route path='/verify-email' element={<VerifyEmail/>}/>
            <Route path='/register'  element={
                <PrivateRegisterRoute>
                    <Register />
                </PrivateRegisterRoute>
            }/>
            <Route path='/registers' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_COORDENADOR', 'ROLE_SECRETARIA', 'ROLE_ADMIN_IGREJA']}><Cadastro/></PrivateRoute>}/>

            <Route path='/home' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_COORDENADOR', 'ROLE_PROFESSOR', 'ROLE_ALUNO', 'ROLE_SECRETARIA', 'ROLE_ADMIN_IGREJA']}><Home/></PrivateRoute>}/>
            <Route path='/igrejas' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA']}><Igreja/></PrivateRoute>}/>
            <Route path='/igrejas/new/:igrejaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewIgreja/></PrivateRoute>}/>
            <Route path='/igrejas/:igrejaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewIgreja/></PrivateRoute>}/>
            <Route path='/igrejaEebd/' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><IgrejaAndEbd/></PrivateRoute>}/>
            <Route path='/igrejaEebd/new/:igrejaEbdID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewIgrejaEbd/></PrivateRoute>}/>
            <Route path='/igrejaEebd/:igrejaEbdID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewIgrejaEbd/></PrivateRoute>}/>
            <Route path='/igrejaEebdEturma/:id' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><IgrejaEbdTurmaFind /></PrivateRoute>} />

            <Route path='/alunos' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA', 'ROLE_COORDENADOR','ROLE_PROFESSOR']}><Aluno/></PrivateRoute>}/>
            <Route path='/alunos/new/:alunoID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_COORDENADOR','ROLE_ADMIN_IGREJA','ROLE_PROFESSOR']}><NewAluno/></PrivateRoute>}/>
            <Route path='/alunos/:alunoID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_COORDENADOR']}><NewAluno/></PrivateRoute>}/>

            <Route path='/alunosEturmas' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA', 'ROLE_COORDENADOR','ROLE_PROFESSOR']}><AlunoAndTurmas/></PrivateRoute>}/>
            <Route path='/alunosEturmas/new/:alunoTurmaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_COORDENADOR','ROLE_PROFESSOR']}><NewAlunoTurma/></PrivateRoute>}/>
            <Route path='/alunosEturmas/:alunoTurmaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_COORDENADOR','ROLE_PROFESSOR']}><NewAlunoTurma/></PrivateRoute>}/>
            <Route path='/editarAlunosEturmas/:alunoTurmaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_COORDENADOR']}><EditAlunoTurma/></PrivateRoute>}/>


            <Route path='/alunosEaulas' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA', 'ROLE_COORDENADOR','ROLE_PROFESSOR']}><AlunoAndAulas/></PrivateRoute>}/>
            <Route path='/alunosEaulas/new/:alunoAulaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_COORDENADOR']}><NewAlunoAula/></PrivateRoute>}/>
            <Route path='/alunosEaulas/:alunoAulaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_COORDENADOR']}><NewAlunoAula/></PrivateRoute>}/>
            <Route path='/alunosAndaulas' element={<PrivateRoute allowedProfiles={['ROLE_ALUNO', 'ROLE_COORDENADOR']}><AulasPorAluno /></PrivateRoute>}/>

            <Route path='/aulas' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_PROFESSOR', 'ROLE_ADMIN_IGREJA', 'ROLE_COORDENADOR']}><Aula/></PrivateRoute>}/>
            <Route path='/aulas/new/:aulaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_PROFESSOR', 'ROLE_ADMIN_IGREJA', 'ROLE_COORDENADOR']}><NewAula/></PrivateRoute>}/>
            <Route path='/aulas/:aulaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_PROFESSOR', 'ROLE_ADMIN_IGREJA', 'ROLE_COORDENADOR']}><NewAula/></PrivateRoute>}/>

            <Route path='/aulasEturmas' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA','ROLE_PROFESSOR']}><AulaAndTurmas/></PrivateRoute>}/>
            <Route path='/aulasEturmas/new/:aulaTurmaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewAulaTurma/></PrivateRoute>}/>
            <Route path='/aulasEturmas/:aulaTurmaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewAulaTurma/></PrivateRoute>}/>

            <Route path='/escolabiblica' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><Ebd/></PrivateRoute>}/>
            <Route path='/escolabiblica/new/:ebdID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewEbd/></PrivateRoute>}/>
            <Route path='/escolabiblica/:ebdID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewEbd/></PrivateRoute>}/>

            <Route path='/escolabiblicaEClasses' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><EbdAndTurmas/></PrivateRoute>}/>
            <Route path='/escolabiblicaEClasses/new/:ebdTurmaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewEbdTurmas/></PrivateRoute>}/>
            <Route path='/escolabiblicaEClasses/:ebdTurmaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewEbdTurmas/></PrivateRoute>}/>

            <Route path='/professores' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA', 'ROLE_ADMIN_IGREJA']}><Professor/></PrivateRoute>}/>
            <Route path='/professores/new/:professorID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA']}><NewProfessor/></PrivateRoute>}/>
            <Route path='/professores/:professorID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA']}><NewProfessor/></PrivateRoute>}/>

            <Route path='/professorEturmas' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_PROFESSOR', 'ROLE_ADMIN_IGREJA']}><ProfessorAndTurmas/></PrivateRoute>}/>
            <Route path='/professorEturmas/new/:professorTurmaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewProfessorTurmas/></PrivateRoute>}/>
            <Route path='/professorEturmas/:professorTurmaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewProfessorTurmas/></PrivateRoute>}/>
            
            <Route path='/professores/aulas' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA']}><ProfessorTurma/></PrivateRoute>}/>
            <Route path='/professorEaulas' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA']}><ProfessorAndAulas/></PrivateRoute>}/>
            <Route path='/professorEaulas/new/:professorAulaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewProfessorAula/></PrivateRoute>}/>
            <Route path='/professorEaulas/:professorAulaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><NewProfessorAula/></PrivateRoute>}/>

            <Route path='/classes' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA', 'ROLE_PROFESSOR', 'ROLE_COORDENADOR']}><Turma/></PrivateRoute>}/>
            <Route path='/classes/:classeID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA', 'ROLE_PROFESSOR', 'ROLE_COORDENADOR']}><NewTurma/></PrivateRoute>}/>
            <Route path='/classes/new/:classeID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA', 'ROLE_PROFESSOR', 'ROLE_COORDENADOR']}><NewTurma/></PrivateRoute>}/>

            <Route path= '/vinculos' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_PROFESSOR', 'ROLE_ALUNO', 'ROLE_ADMIN_IGREJA']}><Vinculos/></PrivateRoute>}/>
            <Route path= '/buscatrimestre' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA', 'ROLE_COORDENADOR']}><DataTableTrimestre/></PrivateRoute>}/>
            <Route path= '/ofertas' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_ADMIN_IGREJA', 'ROLE_SECRETARIA']}><Ofertas/></PrivateRoute>}/>
            <Route path= '/matriculas' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><Matriculas/></PrivateRoute>}/>
            <Route path= '/matriculasteste' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><Matricula/></PrivateRoute>}/>

            <Route path= '/revistas' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA']}><Revistas/></PrivateRoute>}/>
            <Route path= '/revistas/new/:revistaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA']}><NewRevista/></PrivateRoute>}/>
            <Route path= '/revistas/:revistaID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA']}><NewRevista/></PrivateRoute>}/>

            <Route path= '/pedidos' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA', 'ROLE_ADMIN_IGREJA', 'ROLE_COORDENADOR']}><Pedidos/></PrivateRoute>}/>
            <Route path= '/pedidos/new/:pedidoID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA', 'ROLE_ADMIN_IGREJA', 'ROLE_COORDENADOR']}><NewPedidos/></PrivateRoute>}/>
            <Route path= '/pedidos/:pedidoID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA', 'ROLE_ADMIN_IGREJA', 'ROLE_COORDENADOR']}><NewPedidos/></PrivateRoute>}/>

            <Route path= '/pagamentos' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA', 'ROLE_ADMIN_IGREJA']}><Pagamentos/></PrivateRoute>}/>

            <Route path= '/parcelas' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA', 'ROLE_ADMIN_IGREJA']}><Parcelas/></PrivateRoute>}/>

            <Route path= '/listausuarios' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA']}><Usuarios/></PrivateRoute>}/>
            <Route path= '/usuariosedicao/:usuarioID' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA']}><EditUsuario/></PrivateRoute>}/>
            <Route path= '/vincularusuario' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><VincularUsuario/></PrivateRoute>}/>

            <Route path= '/vincularperfil' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN']}><VincularPerfil/></PrivateRoute>}/>

            <Route path= '/relatorios' element={<PrivateRoute allowedProfiles={['ROLE_ADMIN', 'ROLE_SECRETARIA', 'ROLE_ADMIN_IGREJA']}><Report/></PrivateRoute>}/>

        </Routes>
    )
}

export default MainRoutes;