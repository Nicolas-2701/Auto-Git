import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Scanner;
import java.io.*;

public class Auto_git extends JFrame {
    private boolean comandosCarregados = false;
    private String executador = "";
    private String pathS = "";
    private String repoUrlS = "";

    public Auto_git() {
        // variaveis
        // executador
        try (Scanner scanner = new Scanner(new File("executador.txt"))) {
            executador = scanner.nextLine();
        } catch (FileNotFoundException ex) {
            System.err.println("Arquivo não encontrado: " + ex.getMessage());
        }
        // pathS
        try (Scanner scanner = new Scanner(new File("config.txt"))) {
            pathS = scanner.nextLine();
            String[] temp = pathS.split("\\|");
            pathS = temp[1];
        } catch (FileNotFoundException ex) {
            System.err.println("Arquivo não encontrado: " + ex.getMessage());
        }
        // repoUrlS
        try (Scanner scanner = new Scanner(new File("config.txt"))) {
            repoUrlS = scanner.nextLine();
            String[] temp = repoUrlS.split("\\|");
            repoUrlS = temp[2];
        } catch (FileNotFoundException ex) {
            System.err.println("Arquivo não encontrado: " + ex.getMessage());
        }

        // Configurações básicas da janela
        setTitle("Auto Git");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Cria um painel principal para organizar os componentes
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.darkGray);
        add(panel);

        // Menu lateral
        JPanel menu = new JPanel(new GridLayout(5, 1));

        // Pagina que aparece o conteudo
        JPanel page = new JPanel(new BorderLayout());

        // Pages
        // -home
        JPanel home = new JPanel(new BorderLayout());
        JPanel telaHome = new JPanel();
        telaHome.setLayout(new BoxLayout(telaHome, BoxLayout.Y_AXIS));
        telaHome.setBackground(Color.BLACK);
        JScrollPane scrollPaneTelaHome = new JScrollPane(telaHome);
        scrollPaneTelaHome.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneTelaHome.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneTelaHome.getVerticalScrollBar().setUnitIncrement(10);
        scrollPaneTelaHome.setPreferredSize(new Dimension(100, 100));
        JPanel comandosHome = new JPanel();
        comandosHome.setLayout(new BoxLayout(comandosHome, BoxLayout.Y_AXIS));
        JScrollPane scrollPaneComandosHome = new JScrollPane(comandosHome);
        scrollPaneComandosHome.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneComandosHome.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneComandosHome.getVerticalScrollBar().setUnitIncrement(20);
        try (Scanner scanner = new Scanner(new File("Executador.txt"))) {
            String comandosString = scanner.nextLine();// uma unica linha contendo todos os comandos
            String[] comandosArray = comandosString.split("\\|");
            for (int v = 0; v < comandosArray.length; v++) {
                String[] arrayTemp = comandosArray[v].split(";");
                JPanel linha = new JPanel(new GridLayout(1, 2));
                linha.setPreferredSize(new Dimension(250, 50));
                linha.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                JButton temp = new JButton(arrayTemp[0]);
                temp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(arrayTemp[1]);
                    }
                });
                JButton delete = new JButton("X");
                delete.setBackground(Color.RED);
                delete.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try (Scanner scanner = new Scanner(new File("Executador.txt"))) {
                            String mod = scanner.nextLine();
                            mod = mod.replace(arrayTemp[0] + ";" + arrayTemp[1] + "|", ""); // <- Corrigido

                            try (FileWriter writer = new FileWriter("Executador.txt", false)) {
                                writer.write(mod);
                                executador = mod; // <- Corrigido para evitar erro ao ler `scanner.nextLine()`
                            } catch (IOException ex) { // <- Mudado de FileNotFoundException para IOException
                                System.err.println("Erro ao escrever no arquivo: " + ex.getMessage());
                            }
                        } catch (FileNotFoundException ex) {
                            System.err.println("Arquivo não encontrado: " + ex.getMessage());
                        }
                    }
                });
                linha.add(temp);
                linha.add(delete);
                comandosHome.add(linha);
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Arquivo não encontrado: " + ex.getMessage());
        }

        // -comandos
        JPanel comandos = new JPanel(new BorderLayout());

        // -config
        JPanel config = new JPanel();
        config.setLayout(new BoxLayout(config, BoxLayout.Y_AXIS));
        JPanel configPath = new JPanel(new BorderLayout());
        JPanel configRepoUrl = new JPanel(new BorderLayout());

        // Add as sub telas
        // -Panel(principal)
        panel.add(menu, BorderLayout.WEST);
        panel.add(page, BorderLayout.CENTER);
        // -Comandos

        // -home
        home.add(scrollPaneTelaHome, BorderLayout.NORTH);
        home.add(scrollPaneComandosHome);

        // Botões
        JButton homeButton = new JButton("Home");
        JButton comandosButton = new JButton("Comandos");
        JButton sair = new JButton("Sair");
        JButton configButton = new JButton("Config");
        JButton applyPath = new JButton("Apply");
        JButton applyRepoUrl = new JButton("Apply");
        // add Botões
        // -Menu
        menu.add(homeButton);
        menu.add(comandosButton);
        menu.add(configButton);
        menu.add(sair);
        // -Home

        // -config
        JTextArea path = new JTextArea(pathS);
        path.setOpaque(false);
        configPath.add(path);
        configPath.add(applyPath, BorderLayout.EAST);
        config.add(configPath);
        JTextArea repoUrl = new JTextArea(repoUrlS);
        repoUrl.setOpaque(false);
        configRepoUrl.add(repoUrl);
        configRepoUrl.add(applyRepoUrl, BorderLayout.EAST);
        config.add(configRepoUrl);

        // Button functions
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                page.removeAll();
                try (Scanner scanner = new Scanner(new File("Executador.txt"))) {
                    home.removeAll();
                    comandosHome.removeAll();
                    String comandosString = scanner.nextLine();// uma unica linha contendo todos os comandos
                    String[] comandosArray = comandosString.split("\\|");
                    for (int v = 0; v < comandosArray.length; v++) {
                        String[] arrayTemp = comandosArray[v].split(";");
                        JPanel linha = new JPanel(new GridLayout(1, 2));
                        linha.setPreferredSize(new Dimension(250, 50));
                        linha.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        JButton temp = new JButton(arrayTemp[0]);
                        if (v == 0) {
                            continue;
                        }
                        temp.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(arrayTemp[1]. contains("<url-do-repositório>")){
                                    arrayTemp[1] = arrayTemp[1].replace("<url-do-repositório>", repoUrlS);
                                    System.out.println("teste");
                                }
                                try {
                                    ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
                                            "cd " + pathS + " && " + arrayTemp[1]);
                                    builder.redirectErrorStream(true);
                                    Process process = builder.start();

                                    // Lê a saída do comando
                                    BufferedReader reader = new BufferedReader(
                                            new InputStreamReader(process.getInputStream()));
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        JLabel tempLabel = new JLabel(line + "\n");
                                        tempLabel.setForeground(Color.GREEN);
                                        telaHome.add(tempLabel);
                                        panel.revalidate();
                                        panel.repaint();
                                        System.out.println(line); // Exibe a saída do terminal
                                    }

                                    process.waitFor(); // Aguarda o processo terminar
                                } catch (IOException | InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                System.out.println(pathS);
                            }
                        });
                        JButton delete = new JButton("X");
                        delete.setBackground(Color.RED);
                        delete.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try (Scanner scanner = new Scanner(new File("Executador.txt"))) {
                                    String mod = scanner.nextLine();
                                    mod = mod.replace(arrayTemp[0] + ";" + arrayTemp[1] + "|", ""); // <- Corrigido

                                    try (FileWriter writer = new FileWriter("Executador.txt", false)) {
                                        writer.write(mod);
                                        executador = mod; // <- Corrigido para evitar erro ao ler `scanner.nextLine()`
                                        panel.revalidate();
                                        panel.repaint();
                                    } catch (IOException ex) { // <- Mudado de FileNotFoundException para IOException
                                        System.err.println("Erro ao escrever no arquivo: " + ex.getMessage());
                                    }
                                } catch (FileNotFoundException ex) {
                                    System.err.println("Arquivo não encontrado: " + ex.getMessage());
                                }
                            }
                        });
                        linha.add(temp);
                        linha.add(delete);
                        comandosHome.add(linha);
                    }
                } catch (FileNotFoundException ex) {
                    System.err.println("Arquivo não encontrado: " + ex.getMessage());
                }
                home.add(scrollPaneTelaHome, BorderLayout.NORTH);
                home.add(scrollPaneComandosHome);
                page.add(home);
                panel.revalidate();
                panel.repaint();
            }
        });
        comandosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                page.removeAll();
                if (!comandosCarregados) {
                    String caminhoArquivo = "Comandos.txt";
                    try (Scanner scanner = new Scanner(new File(caminhoArquivo))) {
                        String comandosString = scanner.nextLine();// uma unica linha contendo todos os comandos
                        String[] comandosArray = comandosString.split("\\|");
                        JPanel comandosBtn = new JPanel();
                        comandosBtn.setLayout(new BoxLayout(comandosBtn, BoxLayout.Y_AXIS));
                        JPanel info = new JPanel(new BorderLayout());
                        for (int v = 0; v < comandosArray.length; v++) {
                            String[] arrayTemp = comandosArray[v].split(";");
                            JPanel linha = new JPanel(new BorderLayout());
                            linha.setPreferredSize(new Dimension(250, 50));
                            linha.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                            JButton temp = new JButton(arrayTemp[0]);
                            temp.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e2) {
                                    info.removeAll();
                                    JTextArea textArea = new JTextArea(arrayTemp[1]);
                                    textArea.setLineWrap(true);
                                    textArea.setWrapStyleWord(true);
                                    textArea.setEditable(false);
                                    textArea.setOpaque(false);
                                    info.add(textArea);
                                    JButton add = new JButton("ADD");
                                    add.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e3) {
                                            try (Scanner scanner = new Scanner(new File(
                                                    "Executador.txt"))) {
                                                executador = scanner.nextLine();
                                            } catch (FileNotFoundException ex) {
                                                System.err.println("Arquivo não encontrado: " + ex.getMessage());
                                            }
                                            if (!executador.contains(arrayTemp[0] + ";" + arrayTemp[2] + "|")) {
                                                try (FileWriter writer = new FileWriter(
                                                        "Executador.txt",
                                                        true)) {
                                                    writer.write(arrayTemp[0] + ";" + arrayTemp[2] + "|");
                                                    try (Scanner scanner = new Scanner(new File(
                                                            "Executador.txt"))) {
                                                        executador = scanner.nextLine();
                                                    } catch (FileNotFoundException ex) {
                                                        System.err
                                                                .println("Arquivo não encontrado: " + ex.getMessage());
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                                    info.add(add, BorderLayout.SOUTH);
                                    panel.revalidate();
                                    panel.repaint();
                                }
                            });
                            linha.add(temp);
                            comandosBtn.add(linha);
                        }
                        JScrollPane scrollPane = new JScrollPane(comandosBtn);
                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
                        JScrollPane scrollPane2 = new JScrollPane(info);
                        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                        scrollPane2.getVerticalScrollBar().setUnitIncrement(20);
                        comandos.add(scrollPane, BorderLayout.WEST);
                        comandos.add(scrollPane2, BorderLayout.CENTER);
                        comandosCarregados = true;
                    } catch (FileNotFoundException ex) {
                        System.err.println("Arquivo não encontrado: " + ex.getMessage());
                    }
                }
                page.add(comandos);
                panel.revalidate();
                panel.repaint();
            }
        });
        sair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                page.removeAll();
                page.add(config);
                panel.revalidate();
                panel.repaint();
            }
        });
        applyPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pathS = path.getText();
                try (FileWriter writer = new FileWriter("Config.txt", false)) {
                    writer.write("0|" + pathS + "|" + repoUrlS + "|");
                } catch (IOException ex) { // <- Mudado de FileNotFoundException para IOException
                    System.err.println("Erro ao escrever no arquivo: " + ex.getMessage());
                }
                try {
                    ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd " + pathS + " && dir");
                    builder.redirectErrorStream(true);
                    Process process = builder.start();

                    // Lê a saída do comando
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line); // Exibe a saída do terminal
                    }

                    process.waitFor(); // Aguarda o processo terminar
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        applyRepoUrl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repoUrlS = repoUrl.getText();
                try (FileWriter writer = new FileWriter("Config.txt", false)) {
                    writer.write("0|" + pathS + "|" + repoUrlS + "|");
                } catch (IOException ex) { // <- Mudado de FileNotFoundException para IOException
                    System.err.println("Erro ao escrever no arquivo: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        // Cria e exibe a interface gráfica
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Auto_git().setVisible(true);
            }
        });
    }
}