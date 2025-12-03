using System;
using System.Collections.Generic;
using System.Linq;

public class Person
{
    public int Id { get; set; }
    public string Name { get; set; } = "";
    public string Telephone { get; set; } = "";
    public string Email { get; set; } = "";
    public string Role { get; set; } = "";

    public Person(int id, string name, string telephone, string email)
    {
        Id = id;
        Name = name;
        Telephone = telephone;
        Email = email;
    }

    public virtual string GetDetails()
    {
        return $"ID: {Id}, Name: {Name}, Telephone: {Telephone}, Email: {Email}";
    }
}

public class Teacher : Person
{
    public decimal Salary { get; set; }
    public string Subject1 { get; set; } = "";
    public string Subject2 { get; set; } = "";

    public Teacher(int id, string name, string telephone, string email, decimal salary, string s1, string s2)
        : base(id, name, telephone, email)
    {
        Salary = salary;
        Subject1 = s1;
        Subject2 = s2;
        Role = "Teacher";
    }

    public override string GetDetails()
    {
        return $"{base.GetDetails()}, Salary: {Salary}, Subject 1: {Subject1}, Subject 2: {Subject2}";
    }
}

public class Admin : Person
{
    public decimal Salary { get; set; }
    public bool IsFullTime { get; set; }
    public string WorkingHours { get; set; } = "";

    public Admin(int id, string name, string telephone, string email, decimal salary, bool isFullTime, string hours)
        : base(id, name, telephone, email)
    {
        Salary = salary;
        IsFullTime = isFullTime;
        WorkingHours = hours;
        Role = "Admin";
    }

    public override string GetDetails()
    {
        string type = IsFullTime ? "Full-Time" : "Part-Time";
        return $"{base.GetDetails()}, Salary: {Salary}, Type: {type}, Hours: {WorkingHours}";
    }
}

public class Student : Person
{
    public string Subject1 { get; set; } = "";
    public string Subject2 { get; set; } = "";
    public string Subject3 { get; set; } = "";

    public Student(int id, string name, string telephone, string email, string s1, string s2, string s3)
        : base(id, name, telephone, email)
    {
        Subject1 = s1;
        Subject2 = s2;
        Subject3 = s3;
        Role = "Student";
    }

    public override string GetDetails()
    {
        return $"{base.GetDetails()}, Subjects: {Subject1}, {Subject2}, {Subject3}";
    }
}

class Program
{
    static List<Person> records = new List<Person>();

    static List<string> Subjects = new List<string>
    {
        "Math", "English", "Physics", "Chemistry", "Computing"
    };

    static void Main()
    {
        while (true)
        {
            Console.WriteLine("\n===== DESKTOP INFORMATION SYSTEM =====");
            Console.WriteLine("1. Add New Record");
            Console.WriteLine("2. View All Records");
            Console.WriteLine("3. View Records by Role");
            Console.WriteLine("4. Edit Record");
            Console.WriteLine("5. Delete Record");
            Console.WriteLine("6. Exit");

            Console.Write("Select option: ");
            string? choice = Console.ReadLine();

            switch (choice)
            {
                case "1": AddRecord(); break;
                case "2": ViewAll(); break;
                case "3": ViewByRole(); break;
                case "4": EditRecord(); break;
                case "5": DeleteRecord(); break;
                case "6": return;
                default: Console.WriteLine("Invalid option."); break;
            }
        }
    }

    static bool NameExists(string name)
        => records.Any(r => r.Name.ToLower() == name.ToLower());

    static bool EmailExists(string email)
        => records.Any(r => r.Email.ToLower() == email.ToLower());

    static bool IdExists(int id)
        => records.Any(r => r.Id == id);

    static bool IsValidEmail(string email)
        => email.Contains("@") && email.Contains(".");

    static bool IsValidTelephone(string tel)
        => tel.All(char.IsDigit) && tel.Length == 10;

    static string ChooseSubject(List<string> exclude = null)
    {
        exclude ??= new List<string>();
        while (true)
        {
            Console.WriteLine("\nAvailable Subjects:");
            for (int i = 0; i < Subjects.Count; i++)
            {
                if (!exclude.Contains(Subjects[i]))
                    Console.WriteLine($"{i + 1}. {Subjects[i]}");
            }
            Console.Write("Choose subject by number: ");
            string? input = Console.ReadLine();
            if (int.TryParse(input, out int choice) && choice >= 1 && choice <= Subjects.Count)
            {
                string selected = Subjects[choice - 1];
                if (!exclude.Contains(selected)) return selected;
                Console.WriteLine("You already selected this subject. Choose another.");
            }
            else
            {
                Console.WriteLine("Invalid choice. Try again.");
            }
        }
    }

    static void AddRecord()
    {
        int id;
        while (true)
        {
            Console.Write("Enter ID: ");
            if (int.TryParse(Console.ReadLine(), out id))
            {
                if (!IdExists(id)) break;
                Console.WriteLine("ID already exists. Enter a unique ID.");
            }
            else
            {
                Console.WriteLine("Invalid ID. Enter a number.");
            }
        }

        string name;
        while (true)
        {
            Console.Write("Enter Name: ");
            name = Console.ReadLine() ?? "";
            if (string.IsNullOrEmpty(name)) continue;
            if (!NameExists(name)) break;
            Console.WriteLine("Name already exists. Enter another.");
        }

        string tel;
        while (true)
        {
            Console.Write("Enter Telephone (10 digits): ");
            tel = Console.ReadLine() ?? "";
            if (IsValidTelephone(tel)) break;
            Console.WriteLine("Invalid telephone.");
        }

        string email;
        while (true)
        {
            Console.Write("Enter Email: ");
            email = Console.ReadLine() ?? "";
            if (!IsValidEmail(email))
            {
                Console.WriteLine("Invalid email format.");
                continue;
            }
            if (EmailExists(email))
            {
                Console.WriteLine("Email already exists.");
                continue;
            }
            break;
        }

        string role = "";
        while (true)
        {
            Console.WriteLine("Select Role: 1. Teacher 2. Admin 3. Student");
            string? r = Console.ReadLine();
            if (r == "1") { role = "teacher"; break; }
            if (r == "2") { role = "admin"; break; }
            if (r == "3") { role = "student"; break; }
            Console.WriteLine("Invalid choice. Choose 1, 2, or 3.");
        }

        if (role == "teacher")
        {
            Console.Write("Enter Salary: ");
            decimal salary = decimal.Parse(Console.ReadLine() ?? "0");
            string s1 = ChooseSubject();
            string s2 = ChooseSubject(new List<string> { s1 });
            records.Add(new Teacher(id, name, tel, email, salary, s1, s2));
        }
        else if (role == "admin")
        {
            Console.Write("Enter Salary: ");
            decimal salary = decimal.Parse(Console.ReadLine() ?? "0");
            Console.Write("Full-Time? (yes/no): ");
            bool full = (Console.ReadLine() ?? "").ToLower() == "yes";
            Console.Write("Enter Working Hours: ");
            string hours = Console.ReadLine() ?? "";
            records.Add(new Admin(id, name, tel, email, salary, full, hours));
        }
        else if (role == "student")
        {
            string s1 = ChooseSubject();
            string s2 = ChooseSubject(new List<string> { s1 });
            string s3 = ChooseSubject(new List<string> { s1, s2 });
            records.Add(new Student(id, name, tel, email, s1, s2, s3));
        }

        Console.WriteLine("Record added successfully.");
    }

    static void ViewAll()
    {
        if (!records.Any()) { Console.WriteLine("No records found."); return; }
        foreach (var r in records) Console.WriteLine(r.GetDetails());
    }

    static void ViewByRole()
    {
        Console.WriteLine("Select Role: 1. Teacher 2. Admin 3. Student");
        string? r = Console.ReadLine();
        string role = r == "1" ? "teacher" : r == "2" ? "admin" : "student";
        var filter = records.Where(x => x.Role.ToLower() == role);
        foreach (var rec in filter) Console.WriteLine(rec.GetDetails());
    }

    static void EditRecord()
    {
        Console.Write("Enter ID to edit: ");
        if (!int.TryParse(Console.ReadLine(), out int id))
        {
            Console.WriteLine("Invalid ID."); return;
        }
        var record = records.FirstOrDefault(r => r.Id == id);
        if (record == null) { Console.WriteLine("Record not found."); return; }

        Console.Write("New Name (leave blank): ");
        string? name = Console.ReadLine();
        if (!string.IsNullOrEmpty(name) && !NameExists(name)) record.Name = name;

        Console.Write("New Telephone (10 digits, leave blank): ");
        string? tel = Console.ReadLine();
        if (!string.IsNullOrEmpty(tel) && IsValidTelephone(tel)) record.Telephone = tel;

        Console.Write("New Email (leave blank): ");
        string? email = Console.ReadLine();
        if (!string.IsNullOrEmpty(email) && IsValidEmail(email) && !EmailExists(email)) record.Email = email;

        if (record is Teacher t)
        {
            Console.Write("New Salary (leave blank): ");
            string? sal = Console.ReadLine();
            if (!string.IsNullOrEmpty(sal)) t.Salary = decimal.Parse(sal);
            Console.Write("Change Subject 1? (yes/no): ");
            if ((Console.ReadLine() ?? "").ToLower() == "yes") t.Subject1 = ChooseSubject();
            Console.Write("Change Subject 2? (yes/no): ");
            if ((Console.ReadLine() ?? "").ToLower() == "yes") t.Subject2 = ChooseSubject(new List<string> { t.Subject1 });
        }
        else if (record is Admin a)
        {
            Console.Write("New Salary (leave blank): ");
            string? sal = Console.ReadLine();
            if (!string.IsNullOrEmpty(sal)) a.Salary = decimal.Parse(sal);
            Console.Write("Full-Time? (yes/no): ");
            string? ft = Console.ReadLine();
            if (!string.IsNullOrEmpty(ft)) a.IsFullTime = ft.ToLower() == "yes";
            Console.Write("New Working Hours (leave blank): ");
            string? h = Console.ReadLine();
            if (!string.IsNullOrEmpty(h)) a.WorkingHours = h;
        }
        else if (record is Student s)
        {
            Console.Write("Change Subject 1? (yes/no): ");
            if ((Console.ReadLine() ?? "").ToLower() == "yes") s.Subject1 = ChooseSubject();
            Console.Write("Change Subject 2? (yes/no): ");
            if ((Console.ReadLine() ?? "").ToLower() == "yes") s.Subject2 = ChooseSubject(new List<string> { s.Subject1 });
            Console.Write("Change Subject 3? (yes/no): ");
            if ((Console.ReadLine() ?? "").ToLower() == "yes") s.Subject3 = ChooseSubject(new List<string> { s.Subject1, s.Subject2 });
        }

        Console.WriteLine("Record updated successfully.");
    }

    static void DeleteRecord()
    {
        Console.Write("Enter ID to delete: ");
        if (!int.TryParse(Console.ReadLine(), out int id)) { Console.WriteLine("Invalid ID."); return; }
        var record = records.FirstOrDefault(r => r.Id == id);
        if (record == null) { Console.WriteLine("Record not found."); return; }
        records.Remove(record);
        Console.WriteLine("Record deleted.");
    }
}

