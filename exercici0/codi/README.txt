Per a executar aquest exercici, cal executar les classes ReadServer i WriteServer del package tokenRing.

Els altres packages són:
- models: Conté el servidor base del que hereten els altres servidors i la interfície que aporta els mètodes per a
          iniciar correctament els processos mitjançant fitxers.
- network: Conté la classe Frame necessaria per a transmetre les trames.

Els packages a "ignorar" són:
- dynamicServer: Una primera versió fallida que era complicada y tenia errors (del sistema, no del codi).
- staticServer: Primera aproximació a tokenRing però amb la debilitat de treballar amb un nombre estàtic de nodes.
