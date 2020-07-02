#include<iostream>
#include <fstream>

using namespace std;


void prtm(int l[][5]){
	for(int i=0;i<3;i++){
		for(int j=0;j<5;j++)
			cout<<l[i][j]<<" ";
		cout<<endl;
	}
	cout<<endl<<endl;
}
void sort(int k,int l[][5],int c[5]){
		for(int i=0;i<5;i++){
			int max=l[k][i];
			int maxin=i;
			for(int j=0;j<c[k];j++){
				if(max<=l[k][j]){
					max=l[k][j];
					maxin=j;
				}
			}
			if(maxin!=i){
				l[k][maxin]=l[k][i];
				l[k][i]=max;
			}
		}
}
int winner(int g,int l[][10],int b1,int b2){
		for(int i=0;i<10;i++){
			if(l[g][i]==b1)
				return b1;
			else if(l[g][i]==b2)
				return b2;
		}
	
}
int loser(int n,int c[3],int m[][5],int f[][10]){
	for(int i=9;i>=0;i--){
	
		for(int j=0;j<c[n];j++){
			if(m[n][j]==f[n][i])
				return j;
		}
	}
}

void func(int ci[10][3],int p[10],int cnt[3],int cur[3],int faci[][10],int i,int m[][5]){

	while(1){
	
	
		int k=ci[i][p[i]];
	
		if((m[k][cur[k]]==-1)&&(cnt[k]>cur[k])){
		m[k][cur[k]]=i;
				cur[k]++;
			//p[i]++;
			break;
		}
		else {
			int a=loser(k,cnt,m,faci);
			int l=0,w=0;
			if(winner(k,faci,m[k][a],i)==m[k][a]){
				w=m[k][a];
				l=i;
			}
			else{
				w=i;
				l=m[k][a];
			}
			i=l;
			m[k][a]=w;
			p[i]++;
		}
		
		
	}
//	for(int j=0;j<10;j++)
	//	cout<<p[j]<<" ";
//	cout<<endl;
}

int main(){
	 ifstream input("d:\\apply.txt");

 	if(input.fail()){

  		cout << "파일을 여는 데 실패했습니다." << endl;

  	return 1;

 	}

 int city[10][3];

	 for(int i=0; i<10; i++)
		for(int j=0;j<3;j++)
  			input >> city[i][j];

 	for(int i=0; i<10; i++){
		for(int j=0;j<3;j++)
  			cout << "city[" << i << "]:["<<j<<"] " << city[i][j] << endl;
			cout<<endl;
 	}

	 input.close();
	int pos[10]={0}; 
	int cnt[3]={3,2,5};
	int cur[3]={0};
	int faci[3][10]={{0,6,2,1,9,8,4,3,5,7},
					{4,1,0,7,2,3,6,5,9,8},
					{0,2,9,4,6,1,8,3,7,5}};
	int	match[3][5]={{-1,-1,-1,-1,-1},
			 	     {-1,-1,-1,-1,-1},
			 	     {-1,-1,-1,-1,-1}}; 
	func(city,pos,cnt,cur,faci,0,match);
	
	
	func(city,pos,cnt,cur,faci,1,match);
	
	func(city,pos,cnt,cur,faci,2,match);
	func(city,pos,cnt,cur,faci,3,match);
	func(city,pos,cnt,cur,faci,4,match);
	func(city,pos,cnt,cur,faci,5,match);
	func(city,pos,cnt,cur,faci,6,match);
	func(city,pos,cnt,cur,faci,7,match);
	func(city,pos,cnt,cur,faci,8,match);
	func(city,pos,cnt,cur,faci,9,match);
	//func(city,pos,cnt,cur,faci,10,match);
	prtm(match);
}
