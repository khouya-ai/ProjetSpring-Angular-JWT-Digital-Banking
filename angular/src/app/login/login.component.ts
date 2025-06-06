import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit{
  formLogin! : FormGroup;
  constructor(private fb:FormBuilder, private authService:AuthService,private router:Router){}

  ngOnInit() : void{
    this.formLogin=this.fb.group({
      username : this.fb.control("admin"),
      password : this.fb.control("azerty123456")
    })
  }

  handleLogin(){
    let username = this.formLogin.value.username;
    let pwd=this.formLogin.value.password;
    this.authService.login(username,pwd).subscribe({
      next: (data) => {
        this.authService.loadProfile(data);
        this.router.navigateByUrl("/admin");
      },
      error: (err) => {
        console.error(err);
      }
    })
  }


}
