package cmps211.example.texteditor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cmps211.example.texteditor.crdt.CRDTAlgorithm;
import cmps211.example.texteditor.crdt.Node;

@SpringBootApplication
public class TexteditorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TexteditorApplication.class, args);

		Node n0 = new Node(-1,'\0',null);
		Node n1 = new Node(1,Instant.now(),'a',null);
		Node n2 = new Node(1,Instant.now().plus(1, ChronoUnit.MILLIS),'b',null);
		Node n3 = new Node(2,Instant.now(),'c',null);

		CRDTAlgorithm algo = new CRDTAlgorithm();
		algo.insertNode(n0,n1);
		algo.insertNode(n1, n2);		
		algo.insertNode(n0, n3);

		System.out.println(algo.getDocumentText(n0));

	}

}
