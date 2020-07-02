import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Stack;
import java.io.IOException;
import java.awt.*;
import javax.swing.*;

//코사라주 이용하면 O(V+E) 시간 복잡도
public class StreetMaintain extends JFrame{
	static int N,M,V,K,o;
	//N=number of rows M=number of columns o=N+M V=2*o K=number of the terms that needs to be satisfied
	static ArrayList<ArrayList<Integer>> G;
	static ArrayList<ArrayList<Integer>> Gr;
	static ArrayList<ArrayList<Integer>> R;
	static Stack<Integer> stack;
	static int[] val;
	static boolean[] visited;
	//constructor for the result window
	StreetMaintain(String title){
			super(title);
			setSize((3+M)*50,(2+N)*50);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			if(check())
				add(new MyPanel(),BorderLayout.CENTER);
			setVisible(true);
		}
		class MyPanel extends JPanel{
			protected void paintComponent(Graphics g) {
				int[] x=new int[M];
				int[] y=new int[N];
				char[] Cx=new char[M];
				char[] Cy=new char[N];
				String str="red=right blue=left gray=down magenta=up";
				char[] l=str.toCharArray();
				g.setColor(Color.RED);
				g.drawChars(l, 0, 9, this.getWidth()-100, 50);
				g.setColor(Color.BLUE);
				g.drawChars(l,10,9,this.getWidth()-100,65);
				g.setColor(Color.GRAY);
				g.drawChars(l,20,9,this.getWidth()-100,80);
				g.setColor(Color.MAGENTA);
				g.drawChars(l, 30, 10, this.getWidth()-100, 95);
				for(int i=0;i<M;i++) {
					x[i]=50+50*i;
					Cx[i]=Integer.toString(i+1).charAt(0);
				}
				for(int j=0;j<N;j++) {
					y[j]=50+50*j;
					Cy[j]=Integer.toString(j+1).charAt(0);
				}
//Color Red stands for RIGHT and the Blue stands for LEFT	
//Gray line represents DOWN and the Magenta represents UP
				for(int i=0;i<N;i++) {
					if(val[i]==1) g.setColor(Color.RED);
					else if(val[i]==0) g.setColor(Color.BLUE);
					g.drawLine(50, y[i], 50+(N-1)*50, y[i]);
					g.drawChars(Cy, i, 1, 40, y[i]);
				}
				for(int j=N;j<o;j++) {
					if(val[j]==1)
						g.setColor(Color.GRAY);
					else if(val[j]==0)
						g.setColor(Color.MAGENTA);
					g.drawLine(x[j-N], 50, x[j-N], 50+(M-1)*50);
					g.drawChars(Cx, j-N, 1, x[j-N], 40);
				}
			}
		}
		//2.add the nodes to the graph G and do a dfs
		//if x is false and  for (x+y) to be true, y has to be true (~x*y).
		//if y is false and for (x+y) to be true, x has to be true(x*~y).
		//G 그래프에 x가 거짓이고 y가 참인 경우 (~x->y) 와 y가 거짓이고 x가 참인 경우(~y->x)를 추가한다.
		//~x는 x+o이고 ~y는 y+o
	static void add(int x,int y) {
		int f1,f2;
		f1=-x;f2=y;
		if(f1<0) f1=o-f1;
		if(f2<0) f2=o-f2;
		G.get(f1-1).add(f2-1);
		Gr.get(f2-1).add(f1-1);
		f1=-y;f2=x;
		if(f1<0) f1=o-f1;
		if(f2<0) f2=o-f2;
		G.get(f1-1).add(f2-1);
		Gr.get(f2-1).add(f1-1);
	}
	//topo() is similar to reverse() and dfs() is to rDfs()
	//topo()와 dfs()로 G의 모든 노드 방문
	static void dfs(int n) {
		visited[n]=true;
		for(int u:G.get(n)) {
			if(!visited[u]) dfs(u);
		}
		stack.push(n);
	}
	static void topo() {
		stack=new Stack<Integer>();
		visited=new boolean[V];
		for(int i=0;i<V;i++) {
			if(!visited[i]) dfs(i);
		}
	}
	//reverse()와 rDfs()로 Gr의 모든 노드 방문
	//add the reversed nodes to the graph Gr and do a dfs 
	static void reverse() {
		visited=new boolean[V];
		R=new ArrayList<ArrayList<Integer>>();
		while(!stack.isEmpty()) {
			int e=stack.pop();
			if(visited[e]) continue;
			ArrayList<Integer> t=new ArrayList<Integer>();
			visited[e]=true;
			t.add(e);
			R.add(t);
			rDfs(e,t);
		}
	}
	static void rDfs(int e,ArrayList<Integer> t) {
		for(int u:Gr.get(e)) {
			if(!visited[u]) {
				visited[u]=true;
				t.add(u);
				rDfs(u,t);
			}
		}
	}
	//3.check if the resulted graph R is valid(checking if x and ~x is in the same group)
	//그래프 R에서 한 노드와 그 노드의 부정이 같은 그룹 안에 있으면 R은 유효하지 않다.
	static boolean check() {
		for(ArrayList<Integer> u:R) {
			int l=u.size();
			for(int i=0;i<l;i++) {
				for(int j=i+1;j<l;j++) {
					int r=Math.abs(u.get(i)-u.get(j));
					if(r==o) return false;
				}
			}
		}
		return true;
	}
	//4.if valid do a topological sort
	//if there is a route from x to ~x, x has to be false and ~x has to be true
	//topological sort=위상정렬(그룹 간의 위상을 구하는 함수)
	//cl은 클러스터 사이의 관계를 나타낸 그래프
	//클러스터의 진입 차수가 0이면 큐에 넣고 아니면 연결된 클러스터 확인 후 진입 차수 감소
	//인덱스가 o보다 크면 참, 작으면 거짓
	static void topoSort() {
		int[] map=new int[V];
		for(int i=0;i<R.size();i++)
			for(int u:R.get(i)) map[u]=i;
		ArrayList<ArrayList<Integer>> cl=new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<R.size();i++)
			cl.add(new ArrayList<Integer>());
		int[] incnt=new int[R.size()];
		for(int i=0;i<V;i++) {
			int c1=map[i];
			for(int u:G.get(i)) {
				int c2=map[u];
				if(c1!=c2) {
					cl.get(c1).add(c2);
					incnt[c2]++;
				}
			}
		}
		val=new int[o];
		for(int i=0;i<o;i++)
			val[i]=-1;
		ArrayList<Integer> q=new ArrayList<Integer>();
		for(int i=0;i<incnt.length;i++)
			if(incnt[i]==0) q.add(i);
		while(!q.isEmpty()) {
			int e=q.remove(0);
			for(int u:R.get(e)) {
				if(u<o&&val[u]!=-1) continue;
				if(u>=o&&val[u-o]!=-1) continue;
				if(u>=o) val[u-o]=1;
				else val[u]=0;
			}
			for(int u:cl.get(e)) {
				incnt[u]--;
				if(incnt[u]==0) q.add(u);
			}
		}
		String r="row\n";
		for(int i=0;i<N;i++) {
			if(val[i]==0) r+="L";
			else if(val[i]==1) r+="R";
		}
		System.out.println(r);
		r="column\n";
		for(int i=N;i<o;i++) {
			if(val[i]==0) r+="U";
			else if(val[i]==1) r+="D";
		}
		System.out.println(r);
	} 
	//1.to be valid, (Vi+Vj)*(Vx+Vy)*... must be true 
	//(Sa*Sd)+(Sb*Sc)=(Sa+Sb)*(Sa+Sc)*(Sd+Sb)*(Sd+Sc)  *:교집합,+:합집합
	//add (Sa*Sd),(Sa*Sc),(Sd*Sb),(Sd*Sc) to the graph G
	/*가로 줄 Sa(시작),Sc(도착) 2개와 세로 줄 Sb(시작),Sd(도착)가 있을 때 시작 점에서 도착 점 까지 갈 수 있는  방법은  Sa와 Sd,Sb와 Sc를 지나는 것 2개 뿐이기 떄문에 
	 * (Sa*Sd)+(Sb*Sc)=(Sa+Sb)*(Sa+Sc)*(Sd+Sb)*(Sd+Sc)라는 식이 나올 수 있음.
	 * (x+y)*(a+b)*...이런 식이 나오는 문제들은 2-SAT 문제로 풀 수 있음
	*/
	static void addStreet(int a,int b,int c,int d) {
		int Sa,Sb,Sc,Sd;
		Sa=a;Sb=b+N;Sc=c;Sd=d+N;
		if(a>c) {
			Sb=-Sb;Sd=-Sd;
		}
		if(b>d) {
			Sa=-Sa;Sc=-Sc;
		}
		add(Sa,Sb);add(Sa,Sc);add(Sd,Sb);add(Sd,Sc);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			StringTokenizer s=new StringTokenizer(br.readLine());
				G=new ArrayList<ArrayList<Integer>>();
				Gr=new ArrayList<ArrayList<Integer>>();
				R=new ArrayList<ArrayList<Integer>>();
				N=Integer.parseInt(s.nextToken());
				M=Integer.parseInt(s.nextToken());
				K=Integer.parseInt(s.nextToken());
				o=N+M;
				V=2*o;
				for(int j=0;j<V;j++) {
					G.add(new ArrayList<Integer>());
					Gr.add(new ArrayList<Integer>());
				}
				for(int j=0;j<K;j++) {
					s=new StringTokenizer(br.readLine());
					int a,b,c,d;
					a=Integer.parseInt(s.nextToken());
					b=Integer.parseInt(s.nextToken());
					c=Integer.parseInt(s.nextToken());
					d=Integer.parseInt(s.nextToken());
					addStreet(a,b,c,d);
				}
				topo();
				reverse();
				if(check()) { 
						topoSort();
						new StreetMaintain("answer");
				}
				else {
					System.out.println("No");
					
				}
				
		}
		catch(IOException e) {
			
		}
		
	}

}
