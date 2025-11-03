import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RoomComponent } from '../room/room.component';
import { HttpClient } from '@angular/common/http';
import * as uuid from 'uuid';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule,RoomComponent,FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  inputId:String="";
  
  constructor(private router: Router, private http: HttpClient) {}

  handleSubmit(event: Event){
    event.preventDefault();
    console.log("User ID submitted:",this.inputId);
    this.enterRoom();
  }
  createRoom(){
    console.log("Creating room for user id:");
    //const roomId = this.http.get('http://localhost:8081/game');
    const roomId = uuid.v4();
    this.inputId = roomId;
    this.enterRoom()
  }

  enterRoom(){
    this.router.navigate(['/room',this.inputId]);
  }
}
