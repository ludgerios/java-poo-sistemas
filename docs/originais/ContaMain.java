
package prova;
import java.util.Scanner;

public class ContaMain {
    
    // ===== Classe interna Conta =====
    static class Conta {
        private int numeroConta;
        private String titular;
        private double saldo;

        // Construtor vazio
        public Conta() {}

        // Construtor completo
        public Conta(int numeroConta, String titular, double saldo) {
            this.numeroConta = numeroConta;
            this.titular = titular;
            this.saldo = saldo;
        }

        // Getters e Setters
        public int getNumeroConta() {
            return numeroConta;
        }

        public void setNumeroConta(int numeroConta) {
            this.numeroConta = numeroConta;
        }

        public String getTitular() {
            return titular;
        }

        public void setTitular(String titular) {
            this.titular = titular;
        }

        public double getSaldo() {
            return saldo;
        }

        public void setSaldo(double saldo) {
            this.saldo = saldo;
        }

        // Métodos de ação
        public void depositarDinheiro(double valor) {
            if (valor > 0) {
                saldo += valor;
            } else {
                System.out.println("Valor inválido para depósito!");
            }
        }

        public boolean sacarDinheiro(double valor) {
            if (valor > 0 && saldo >= valor) {
                saldo -= valor;
                return true;
            } else {
                return false;
            }
        }

        // toString
        @Override
        public String toString() {
            return "Conta Nº: " + numeroConta + 
                   "\nTitular: " + titular + 
                   "\nSaldo: R$ " + String.format("%.2f", saldo);
        }
    }

    // ===== Programa principal =====
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Conta minhaConta = null;
        int opcao;

        do {
            System.out.println("\n===== MENU BANCÁRIO =====");
            System.out.println("1 - Cadastrar conta");
            System.out.println("2 - Mostrar saldo");
            System.out.println("3 - Sacar dinheiro");
            System.out.println("4 - Depositar dinheiro");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");
           
           
            switch (opcao) {
                case 1:
                    System.out.print("Digite o número da conta: ");
                    int numero = sc.nextInt();
                    sc.nextLine(); 
                    System.out.print("Digite o nome do titular: ");
                    String nome = sc.nextLine();
                    System.out.print("Digite o saldo inicial: ");
                    double saldoInicial = sc.nextDouble();

                    minhaConta = new Conta(numero, nome, saldoInicial);
                    System.out.println("\nConta criada com sucesso!");
                    break;

                case 2:
                    if (minhaConta != null) {
                        System.out.println("\n" + minhaConta.toString());
                    } else {
                        System.out.println("\nNenhuma conta cadastrada!");
                    }
                    break;

                case 3:
                    if (minhaConta != null) {
                        System.out.print("Valor para saque: ");
                        double valorSaque = sc.nextDouble();
                        boolean sucesso = minhaConta.sacarDinheiro(valorSaque);

                        if (sucesso) {
                            System.out.println("Saque realizado com sucesso!");
                        } else {
                            System.out.println("Saldo insuficiente ou valor inválido!");
                        }
                    } else {
                        System.out.println("\nNenhuma conta cadastrada!");
                    }
                    break;

                case 4:
                    if (minhaConta != null) {
                        System.out.print("Valor para depósito: ");
                        double valorDeposito = sc.nextDouble();
                        minhaConta.depositarDinheiro(valorDeposito);
                        System.out.println("Depósito realizado com sucesso!");
                    } else {
                        System.out.println("\nNenhuma conta cadastrada!");
                    }
                    break;

                case 5:
                    System.out.println("\nEncerrando o sistema...");
                    break;

                default:
                    System.out.println("\nOpção inválida! Tente novamente.");
            }

        } while (opcao != 5);

        sc.close();
    }
}