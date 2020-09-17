package main.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.entity.Funcionario;
import main.repository.CNXJDBC;
import main.util.ShowAlert;

public class DaoFuncionario extends CNXJDBC {

	private final String SQL_INSERE_FUNCIONARIO = "INSERT INTO Funcionario (Cpf,Nome,Email, Cargo, Data_nascimento, Salario) VALUES (?,?,?,?,?,?);";

	private final String SQL_SELECIONA_FUNCIONARIO = "SELECT * FROM Funcionario";
	
	private final String SQL_ALTERA_FUNCIONARIO = "UPDATE Funcionario SET Cpf = ?, Nome =?, Email = ? , Cargo = ?, Data_nascimento = ?,"
			+ "Salario = ? WHERE Id_funcionario = ?;";
	
	private final String SQL_EXCLUI_FUNCIONARIO = "DELETE FROM Funcionario  WHERE Id_funcionario = ?;";

	private PreparedStatement pst = null;

	public boolean inserirFuncionario(Funcionario funcionario) {
	
		try (Connection conn = this.conexaoBanco(); PreparedStatement pst = conn.prepareStatement(SQL_INSERE_FUNCIONARIO);) {
			pst.setString(1, funcionario.getCpf());
			pst.setString(2, funcionario.getNome());
			pst.setString(3, funcionario.getEmail());
			pst.setString(4, funcionario.getCargo());
			pst.setDate(5, Date.valueOf(funcionario.getData_nascimento()));
			pst.setFloat(6, funcionario.getSalario());
			pst.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
			new ShowAlert().erroAlert("Erro ao editar funcionário: CPF já esta cadastrado!");
			System.out.println("Erro ao executar o Statment " + e.toString());
		}
		
		return false;
	}
	
	public ArrayList<Funcionario> listarFuncionario() {
		ArrayList<Funcionario> listaFuncionario = new ArrayList<Funcionario>();

		Funcionario funcionario;
		try (Connection conn = this.conexaoBanco(); PreparedStatement pst = conn.prepareStatement(SQL_SELECIONA_FUNCIONARIO); ResultSet rs = pst.executeQuery();) {
			while (rs.next()) {
				funcionario = new Funcionario();
				funcionario.setCpf(rs.getString("CPF"));
				funcionario.setNome(rs.getString("NOME"));
				funcionario.setEmail(rs.getString("EMAIL"));
				funcionario.setCargo(rs.getString("CARGO"));
				funcionario.setData_nascimento(rs.getDate("DATA_NASCIMENTO").toLocalDate());
				funcionario.setSalario(rs.getFloat("SALARIO"));
				funcionario.setId(rs.getInt("ID_FUNCIONARIO"));
				listaFuncionario.add(funcionario);
			}

		} catch (SQLException e) {
			System.out.println("Erro ao executar o Statement" + e.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		
		return listaFuncionario;
	}
	
	public boolean alterarFuncionario(Funcionario funcionario) {
		
		try (Connection conn = this.conexaoBanco(); PreparedStatement pst = conn.prepareStatement(SQL_ALTERA_FUNCIONARIO);) {
			pst.setString(1, funcionario.getCpf());
			pst.setString(2, funcionario.getNome());
			pst.setString(3, funcionario.getEmail());
			pst.setString(4, funcionario.getCargo());
			pst.setDate(5, Date.valueOf(funcionario.getData_nascimento()));
			pst.setFloat(6, funcionario.getSalario());
			pst.setInt(7,  funcionario.getId());
			
			pst.execute();
			
			return true;
		} catch (SQLException e) {
			new ShowAlert().erroAlert("Erro ao editar funcionário: CPF já esta cadastrado!");
			System.out.println("Erro ao executar o Statment " + e.toString());
		}	
		
		return false;
	}
	
	 public void excluirFuncionario(Integer Id_funcionario) {
			try (Connection conn = this.conexaoBanco(); PreparedStatement pst = conn.prepareStatement(SQL_EXCLUI_FUNCIONARIO);) {
				pst.setInt(1, Id_funcionario);
				pst.execute();
			} catch (SQLException e) {
				System.out.println("Erro ao executar o Statment " + e.toString());
			}
			
		}
	
	
}
