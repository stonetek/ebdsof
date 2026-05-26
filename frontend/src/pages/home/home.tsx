import BACK from '../../../public/image/ebd3.jpg';
import Header from '../../components/header/header';
//import Button from 'react-bootstrap/Button';
import 'bootstrap/dist/css/bootstrap.min.css';
import Footer from '../../components/footer/footer';
import { useEffect } from 'react';
import Logout from '../login/logout';
import Menu2 from '../../components/menu/componenteMenu';

function Home() {

  useEffect(() => {
  }, []);
  const profileMap: Record<string, 'administrador' | 'admin_igreja' | 'coordenador' | 'secretaria' | 'professor' | 'aluno'> = {
    ROLE_ADMIN: "administrador",
    ROLE_ADMIN_IGREJA: "admin_igreja",
    ROLE_COORDENADOR: "coordenador",
    ROLE_SECRETARIA: "secretaria",
    ROLE_PROFESSOR: "professor",
    ROLE_ALUNO: "aluno",
  };

  type PermissionKey = 'administrador' | 'admin_igreja' | 'coordenador' | 'secretaria' | 'professor' | 'aluno';

  

  const permissions: { [key: string]: string[] } = {
    administrador: ["IGREJAS", "ALUNOS", "AULAS", "ESCOLA BÍBLICA", "PROFESSORES", "CLASSES", "RELATÓRIOS", "TRIMESTRE", 
      "CONTRIBUIÇÕES", "MATRÍCULAS", "CADASTRO", "RELATÓRIO", "REVISTAS", "PEDIDOS"],

    admin_igreja: ["ALUNOS", "AULAS", "PROFESSORES", "CLASSES", "RELATÓRIOS", "TRIMESTRE", "CONTRIBUIÇÕES", 
      "CADASTRO", "RELATÓRIO", "PEDIDOS"],

    coordenador: ["ALUNOS", "AULAS", "CLASSES", "TRIMESTRE", "CADASTRO", "PEDIDOS"],

    secretaria: [ "PROFESSORES", "ALUNOS", "MATRÍCULAS", "RELATÓRIOS", "CADASTRO", "RELATÓRIO", "REVISTAS"],

    professor: ["AULAS", "CLASSES", "ALUNOS",],

    aluno: ["AULAS/ALUNO"]
  };

  const rawProfile = sessionStorage.getItem('userProfile') || 'ROLE_ALUNO';
  const userProfile = (profileMap[rawProfile] ?? 'aluno') as PermissionKey; 

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const userPermissions = permissions[userProfile] ?? [];

  return (
    <>
      <div className="relative z-20">
        <div>
          <Header />
        </div>
        <div className='flex bg-black items-center justify-between px-4 gap-4 flex-wrap'>
          <div className='flex-1 min-w-0'>
            <Menu2/>
          </div>
          <div className='flex-shrink-0'>
            <Logout/>
          </div>  
          
        </div>
      </div>

      <div className="h-screen flex items-center justify-center overflow-hidden relative">
        <img src={BACK} alt="bglogo" className="absolute inset-0 w-full h-full object-cover z-0" />
        <div className="absolute inset-0 flex items-center justify-center">
          <div className="relative z-10">
            {/*<div className='flex gap-20'>
              <div className='flex flex-col text-center w-64 top-10'>

                {userPermissions.includes("IGREJAS") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/igrejas">IGREJAS</Button>}
                {userPermissions.includes("ESCOLA BÍBLICA") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/escolabiblica">ESCOLA BÍBLICA</Button>}
                {userPermissions.includes("PROFESSORES") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/professores">PROFESSORES</Button>}
                {userPermissions.includes("ALUNOS") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/alunos">ALUNOS</Button>}
                {userPermissions.includes("CLASSES") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/classes">CLASSES</Button>}
                
              </div>

              <div className='flex flex-col text-center'>
                
                {userPermissions.includes("AULAS") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/aulas">AULAS</Button>}
                {userPermissions.includes("RELATÓRIOS") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/vinculos">RELATÓRIOS</Button>}
                {userPermissions.includes("TRIMESTRE") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/buscatrimestre">TRIMESTRE</Button>}
                {userPermissions.includes("CONTRIBUIÇÕES") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/ofertas">CONTRIBUIÇÕES</Button>}
                {userPermissions.includes("MATRÍCULAS") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/matriculas">MATRÍCULAS</Button>}
                
              </div>

              <div className='flex flex-col text-center'>

                {userPermissions.includes("AULAS/ALUNO") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/alunosAndaulas">AULAS/ALUNO</Button>}
                {userPermissions.includes("RELATÓRIO") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/relatorios">RELATÓRIO SECRETARIA</Button>}
                {userPermissions.includes("CADASTRO") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/registers">CADASTRO</Button>}
                {userPermissions.includes("REVISTAS") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/revistas">REVISTAS</Button>}
                {userPermissions.includes("PEDIDOS") && <Button variant="primary" className='btn-primary w-48 mb-2' as="a" href="/pedidos">PEDIDOS</Button>}

              </div>
                
              <br />
                
            </div>*/}
            
          </div>
        </div>
      </div>
      <Footer />
    </>
  );

}

export default Home;
