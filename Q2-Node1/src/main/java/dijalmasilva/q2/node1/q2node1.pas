(* Exemplo criado pela documentação oficial
do freepascal.

Link: http://www.freepascal.org/docs-html/rtl/sockets
-->Servidor: http://www.freepascal.org/docs-html/rtl/sockets/accept.html

Outras informações importantes:
- uma string para pascal tem seu primeiro caractere igual ao seu tamanho.
ref.: http://www.baskent.edu.tr/~tkaracay/etudio/ders/prg/pascal/PasHTM1/pas/pasl1007.html



*)

program q2node1;

{$mode fpc}
uses sockets;

const
  PORT = 10999;

  var
    sockadd : TInetSockAddr;
      socklen : TSockLen;
        psocket : Longint;
          csocket : Longint;
            sin, sout : string;
              sinlen : Longint;


              procedure showerror (const emsg:string);
              begin
                //socketerror é uma variável global criada pela
                  //unidade sockets e alterado (o seu valor)
                    //para cada erro. Alguns erros:
                      //
                        //TCP_CONGESTION = 13 (http://www.freepascal.org/docs-html/rtl/sockets/tcp_congestion.html)
                          //
                            writeln (emsg , SocketError);
                              halt(100);
                              end;

                              begin
                                //configuração
                                  sockadd.sin_family:=AF_INET;//tipo de protocolo de transp.
                                    sockadd.sin_port:=htons(PORT);//5000 shl 8;//port 79
                                      sockadd.sin_addr.s_addr:=((1 shl 24) or 127);//127.0.0.1
                                        //criando o socket
                                          psocket:= fpSocket(AF_INET,SOCK_STREAM,0);
                                            //
                                              socklen := sizeof(sockaddr);
                                                //liga o socket ao endereco e porta
                                                  if fpbind(psocket, @sockadd, socklen) = -1 then
                                                      begin
                                                            showerror('Erro ao fazer a ligação do servidor com o protocolo. Erro: ');
                                                                end;
                                                                  //inicia a escuta
                                                                    if fplisten(psocket, 1) = -1 then
                                                                        begin
                                                                              writeln ('Não pode ser feito a escuta na porta ', PORT, '.');
                                                                                  end;
                                                                                    //aguarda a conexao e recupera a conexao
                                                                                      csocket := fpaccept(psocket, @sockadd, @socklen);
                                                                                        if csocket = -1 then
                                                                                            begin
                                                                                                  writeln ('Não conseguiu aguardar a thread de conexão.');
                                                                                                      end;
                                                                                                        //recupera o stream
                                                                                                          sinlen := fprecv(csocket, @sin, sizeof(sin), 4);
                                                                                                            writeln('Mensagem recebida: "', sin, '" com a quantidade de bytes: ', sinlen - 1);
                                                                                                              //escreve no stream
                                                                                                                sout := 'Hello World!';
                                                                                                                  fpsend(csocket, @sout, sizeof(sout), 4);
                                                                                                                    writeln('Mensagem enviada!');
                                                                                                                      //fecha o stream
                                                                                                                        fpshutdown(csocket, 2);
                                                                                                                          //finalization  closeSocket(psocket);

                                                                                                                          end.