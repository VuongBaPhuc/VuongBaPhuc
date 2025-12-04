using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;

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

    // ✅ EMAIL CHUẨN – KHÔNG NHẬN "0"
    static bool IsValidEmail(string email)
    {
        if (string.IsNullOrWhiteSpace(email)) return false;
        email = email.Trim().ToLower();
        if (email.All(char.IsDigit)) return false;

        string pattern = @"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$";
        return Regex.IsMatch(email, pattern);
    }

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
                Console.WriteLine("You already selected this subject.");
            }
            else Console.WriteLine("Invalid choice.");
        }
    }

    // ================= ADD ===================
    static void AddRecord()
    {
        int id;
        while (true)
        {
            Console.Write("Enter ID: ");
            if (int.TryParse(Console.ReadLine(), out id) && !IdExists(id)) break;
            Console.WriteLine("Invalid or duplicate ID.");
        }

        string name;
        while (true)
        {
            Console.Write("Enter Name: ");
            name = Console.ReadLine() ?? "";
            if (!string.IsNullOrWhiteSpace(name) && !NameExists(name)) break;
            Console.WriteLine("Invalid or duplicate Name.");
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
            if (!IsValidEmail(email)) { Console.WriteLine("Invalid email."); continue; }
            if (EmailExists(email)) { Console.WriteLine("Email already exists."); continue; }
            break;
        }

        Console.WriteLine("Select Role: 1. Teacher 2. Admin 3. Student");
        string role = Console.ReadLine() ?? "";

        if (role == "1")
        {
            Console.Write("Enter Salary: ");
            decimal salary = decimal.Parse(Console.ReadLine() ?? "0");
            string s1 = ChooseSubject();
            string s2 = ChooseSubject(new List<string> { s1 });
            records.Add(new Teacher(id, name, tel, email, salary, s1, s2));
        }
        else if (role == "2")
        {
            Console.Write("Enter Salary: ");
            decimal salary = decimal.Parse(Console.ReadLine() ?? "0");
            Console.Write("Full-Time? (true/false): ");
            bool full = bool.Parse(Console.ReadLine() ?? "false");
            Console.Write("Enter Working Hours: ");
            string hours = Console.ReadLine() ?? "";
            records.Add(new Admin(id, name, tel, email, salary, full, hours));
        }
        else if (role == "3")
        {
            string s1 = ChooseSubject();
            string s2 = ChooseSubject(new List<string> { s1 });
            string s3 = ChooseSubject(new List<string> { s1, s2 });
            records.Add(new Student(id, name, tel, email, s1, s2, s3));
        }

        Console.WriteLine("Record added successfully.");
    }

    // ================= VIEW ===================
    static void ViewAll()
    {
        if (!records.Any()) { Console.WriteLine("No records."); return; }
        foreach (var r in records) Console.WriteLine(r.GetDetails());
    }

    static void ViewByRole()
    {
        Console.WriteLine("Select Role: 1. Teacher 2. Admin 3. Student");
        string r = Console.ReadLine() ?? "";
        string role = r == "1" ? "teacher" : r == "2" ? "admin" : "student";
        foreach (var rec in records.Where(x => x.Role.ToLower() == role))
            Console.WriteLine(rec.GetDetails());
    }

    // ================= EDIT ===================
    static void EditRecord()
    {
        Console.Write("Enter ID to edit: ");
        if (!int.TryParse(Console.ReadLine(), out int id)) return;

        var record = records.FirstOrDefault(r => r.Id == id);
        if (record == null) { Console.WriteLine("Record not found."); return; }

        while (true)
        {
            Console.WriteLine("\n=== EDIT MENU ===");
            Console.WriteLine("1. Edit Name");
            Console.WriteLine("2. Edit Telephone");
            Console.WriteLine("3. Edit Email");
            Console.WriteLine("4. Edit Extra 1");
            Console.WriteLine("5. Edit Extra 2");
            Console.WriteLine("6. Edit Extra 3");
            Console.WriteLine("0. Exit");
            Console.Write("Choose: ");

            string c = Console.ReadLine() ?? "";
            if (c == "0") break;

            switch (c)
            {
                case "1":
                    Console.Write("New Name: ");
                    string name = Console.ReadLine() ?? "";
                    if (!NameExists(name)) record.Name = name;
                    break;

                case "2":
                    Console.Write("New Telephone: ");
                    string tel = Console.ReadLine() ?? "";
                    if (IsValidTelephone(tel)) record.Telephone = tel;
                    break;

                case "3":
                    while (true)
                    {
                        Console.Write("New Email: ");
                        string email = (Console.ReadLine() ?? "").Trim().ToLower();
                        if (email == record.Email) break;
                        if (IsValidEmail(email) && !EmailExists(email))
                        {
                            record.Email = email;
                            break;
                        }
                        Console.WriteLine("Invalid or duplicate email.");
                    }
                    break;

                case "4":
                    if (record is Teacher t1)
                    {
                        Console.Write("New Salary: ");
                        t1.Salary = decimal.Parse(Console.ReadLine() ?? "0");
                    }
                    else if (record is Admin a1)
                    {
                        Console.Write("New Salary: ");
                        a1.Salary = decimal.Parse(Console.ReadLine() ?? "0");
                    }
                    else if (record is Student s1)
                        s1.Subject1 = ChooseSubject();
                    break;

                case "5":
                    if (record is Teacher t2)
                        t2.Subject1 = ChooseSubject();
                    else if (record is Admin a2)
                    {
                        Console.Write("Full-Time (true/false): ");
                        a2.IsFullTime = bool.Parse(Console.ReadLine() ?? "false");
                    }
                    else if (record is Student s2)
                        s2.Subject2 = ChooseSubject(new List<string> { s2.Subject1 });
                    break;

                case "6":
                    if (record is Teacher t3)
                        t3.Subject2 = ChooseSubject(new List<string> { t3.Subject1 });
                    else if (record is Admin a3)
                    {
                        Console.Write("New Working Hours: ");
                        a3.WorkingHours = Console.ReadLine() ?? "";
                    }
                    else if (record is Student s3)
                        s3.Subject3 = ChooseSubject(new List<string> { s3.Subject1, s3.Subject2 });
                    break;
            }
        }

        Console.WriteLine("Record updated successfully.");
    }

    // ================= DELETE ===================
    static void DeleteRecord()
    {
        Console.Write("Enter ID to delete: ");
        if (!int.TryParse(Console.ReadLine(), out int id)) return;
        var record = records.FirstOrDefault(r => r.Id == id);
        if (record == null) return;
        records.Remove(record);
        Console.WriteLine("Record deleted.");
    }
}
