export class User {
    id: number;
    firstname: string;
    lastname: string;
    email: string;
    height: string;
    weight: number;
    age: number;
    activityLevel: string;
    goal: string;
    role: string;
    authorities: [];
    startingWeight: number;
    //entries: [];
  
    constructor(){
      this.id = 0;
      this.firstname = '';
      this.lastname = '';
      this.email = '';
      this.height = '';
      this.weight = 0;
      this.age = 0;
      this.activityLevel = '';
      this.goal = '';
      this.role = '';
      this.authorities = [];
      this.startingWeight = 0;
      //this.entries = [];    
    }
  }
  