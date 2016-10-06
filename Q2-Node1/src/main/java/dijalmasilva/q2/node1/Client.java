/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dijalmasilva.q2.node1;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dijalma Silva <dijalmacz@gmail.com>
 */
public class Client {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int i;

        do {
            menu();
            i = in.nextInt();
            runSelect(i, in);
        } while (i != 3);

        System.out.println("");
        System.out.println("Aplicação encerrada.");
    }

    static void menu() {
        System.out.println("--------- Menu ---------");
        System.out.println("1 - Salvar usuário");
        System.out.println("2 - Buscar usuários");
        System.out.println("3 - Sair");
        System.out.println("------------------------");
        System.out.print("Selecione a opção desejada: ");
    }

    static void runSelect(int i, Scanner in) {
        switch (i) {
            case 1:
                System.out.println("");
                System.out.println("------------------------");
                System.out.print("Digite o nome do usuário: ");
                String nome = in.nextLine();
                saveUser(nome);
                break;
            case 2:
                System.out.println("");
                System.out.println("Buscar usuários.");
                System.out.println("");
                findUsers();
                break;
            case 3:
                break;
            default:
                System.out.println("");
                System.out.println("Opção inválida.");
                System.out.println("");
                break;
        }
    }

    static void saveUser(String user) {

        try {
            Socket s = new Socket("localhost", 8888);
            String query = "insert into usuario (nome) values ('"+user+"');";
            s.getOutputStream().write(query.getBytes());
            InputStream in = s.getInputStream();
            byte[] b = new byte[in.available()];
            in.read(b);
            System.out.println(new String(b).trim());
        } catch (IOException ex) {
            System.out.println("Não foi possível se comunicar com a porta");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void findUsers() {

        try {
            Socket s = new Socket("localhost", 8888);
            String query = "select * from user";
            s.getOutputStream().write(query.getBytes());
            InputStream in = s.getInputStream();
            byte[] b = new byte[in.available()];
            in.read(b);
            printUsers(new String(b).trim());
        } catch (IOException ex) {
            System.out.println("Não foi possível se comunicar com a porta");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void printUsers(String users){
        String[] usuarios = users.split(";");
        System.out.println("--------------- Usuários encontrados ---------------");
        for (String u : usuarios) {
            System.out.println(u);
        }
        System.out.println("----------------------------------------------------");
        System.out.println("");
    }
}
