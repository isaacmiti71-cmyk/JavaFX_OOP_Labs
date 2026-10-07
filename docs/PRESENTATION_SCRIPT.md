# 3-Minute Group Presentation Script (4 Speakers)

**Project:** Student Services Desktop App (Register Student Feature)  
**Course:** ICT261 Advanced Java Programming  
**Lecturer:** Mr. E. Nyirenda  
**Institution:** Mulungushi University  
**Group Lead:** Isaac Miti (ID: 202509238)  
**Total Target Time:** 3 Minutes (~400 words)  

---

### Speaker 1: Isaac Miti — Introduction & Feature Overview (45 seconds)
> "Good morning Mr. Nyirenda and colleagues. My name is Isaac Miti, student ID 202509238, presenting on behalf of our group.  
> 
> For our ICT261 practical assignment, we built the **Student Services Desktop App**, focusing on the core feature: **Register Student**. In any university administration system, registering students accurately is essential. Our application provides an intuitive desktop interface where staff can enter a 9-digit Student ID, Full Name, Email, and academic Programme, view registered students in a live table, and store records permanently in an embedded database."

---

### Speaker 2: Architectural Layers & Separation of Concerns (45 seconds)
> "Thank you, Isaac. I am [Speaker 2 Name].  
> 
> Our system strictly implements a **one-way layered architecture** across five clear tiers:  
> 1. The **UI layer** with JavaFX FXML,  
> 2. The **Controller layer** managing UI events,  
> 3. The **Service layer** containing validation rules,  
> 4. The **Repository layer** executing JDBC queries, and  
> 5. The **Database layer** powered by embedded H2.  
> 
> We separated these layers so each component has one single responsibility. The UI never talks directly to the database. This allows us to modify the database or UI independently without breaking our core business rules."

---

### Speaker 3: Sequence Flow & Error Handling (45 seconds)
> "Hello everyone, I am [Speaker 3 Name].  
> 
> When the user clicks **Register**, the Controller extracts the form values and forwards them down to the Service. The Service performs two vital checks: it validates formatting—ensuring a 9-digit numeric ID and valid email—and queries the repository to check for duplicate IDs.  
> 
> If someone tries to re-register an existing student, like ID `202509238`, the Service throws a `DuplicateStudentException`. The Controller intercepts this exception and displays a friendly JavaFX alert. Notice that the application does not crash, and the form data is preserved so the user can easily fix the mistake."

---

### Speaker 4: Live Demonstration & Conclusion (45 seconds)
> "I am [Speaker 4 Name], and I will now walk through our live demo.  
> 
> 1. First, we enter a new student: ID `202509238`, name `Isaac Miti`, email `imiti@mu.ac.zm`, programme `Software Engineering`. Clicking **Register** successfully saves the record and refreshes the table instantly.  
> 2. Second, we test error handling by clicking Register again with the same ID. The system triggers our duplicate warning dialog, keeping the inputs intact.  
> 3. Third, if we enter an invalid email or a short ID, the validation alert flags the issue immediately.  
> 
> In summary, our project demonstrates strict architectural layers, clean JDBC persistence, defensive validation, and resilient error recovery. Thank you, and we welcome your questions!"
