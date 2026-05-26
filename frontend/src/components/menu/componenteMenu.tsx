//import React from "react";

const profileMap: { [key: string]: string } = {
  ROLE_ADMIN: "administrador",
  ROLE_ADMIN_IGREJA: "admin_igreja",
  ROLE_COORDENADOR: "coordenador",
  ROLE_SECRETARIA: "secretaria",
  ROLE_PROFESSOR: "professor",
  ROLE_ALUNO: "aluno",
};

const permissions: { [key: string]: string[] } = {
  administrador: ["IGREJAS", "ALUNOS", "AULAS", "ESCOLA BÍBLICA", "PROFESSORES", "CLASSES", "RELATÓRIOS", "TRIMESTRE", "CONTRIBUIÇÕES", "MATRÍCULAS", "CADASTRO", "RELATÓRIO", "REVISTAS", "PEDIDOS"],
  admin_igreja: ["ALUNOS", "AULAS", "PROFESSORES", "CLASSES", "RELATÓRIOS", "TRIMESTRE", "CONTRIBUIÇÕES", "CADASTRO", "RELATÓRIO", "PEDIDOS"],
  coordenador: ["ALUNOS", "AULAS", "CLASSES", "TRIMESTRE", "CADASTRO", "PEDIDOS"],
  secretaria: ["PROFESSORES", "ALUNOS", "MATRÍCULAS", "RELATÓRIOS", "CADASTRO", "RELATÓRIO", "REVISTAS"],
  professor: ["AULAS", "CLASSES", "ALUNOS"],
  aluno: ["AULAS/ALUNO"]
};

const linkMap: { [key: string]: { label: string; href: string } } = {
  IGREJAS: { label: "Igrejas", href: "/igrejas" },
  "ESCOLA BÍBLICA": { label: "Escola Bíblica", href: "/escolabiblica" },
  PROFESSORES: { label: "Professores", href: "/professores" },
  ALUNOS: { label: "Alunos", href: "/alunos" },
  CLASSES: { label: "Classes", href: "/classes" },
  AULAS: { label: "Aulas", href: "/aulas" },
  RELATÓRIOS: { label: "Relatórios", href: "/vinculos" },
  TRIMESTRE: { label: "Trimestre", href: "/buscatrimestre" },
  CONTRIBUIÇÕES: { label: "Contribuições", href: "/ofertas" },
  //MATRÍCULAS: { label: "Matrículas", href: "/matriculas" },
  CADASTRO: { label: "Cadastro", href: "/registers" },
  RELATÓRIO: { label: "Relatório Secretaria", href: "/relatorios" },
  REVISTAS: { label: "Revistas", href: "/revistas" },
  PEDIDOS: { label: "Pedidos", href: "/pedidos" },
  "AULAS/ALUNO": { label: "Aulas/Aluno", href: "/alunosAndaulas" }
};

export default function Menu2() {
  const rawProfile = sessionStorage.getItem("userProfile") || "ROLE_ALUNO";
  const userProfile = profileMap[rawProfile] || "aluno";
  const userPermissions = permissions[userProfile] ?? [];

  return (
    <nav className="bg-slate-800 text-white">
      <div className="container mx-auto px-4">
        <ul className="flex gap-4 py-3 overflow-x-auto">
          {userPermissions.map((perm) => {
            const link = linkMap[perm];
            if (!link) return null;
            return (
              <li key={perm}>
                <a
                  className="px-3 py-1 rounded hover:bg-slate-700 block whitespace-nowrap"
                  href={link.href}
                >
                  {link.label}
                </a>
              </li>
            );
          })}
        </ul>
      </div>
    </nav>
  );
}